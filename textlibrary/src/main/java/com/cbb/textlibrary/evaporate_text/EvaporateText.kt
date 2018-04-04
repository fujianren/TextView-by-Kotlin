package com.cbb.textlibrary.evaporate_text

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.cbb.textlibrary.base.BaseText
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.base.DefaultAnimatorListener
import com.cbb.textlibrary.utils.CharacterDiffResult
import com.cbb.textlibrary.utils.CharacterUtils
import kotlin.math.max

/**
 * Created by CBB on 2018/4/3.
 * describe: 蒸发效果，文本切换时，新文本从下方升起，旧文本会向上逐渐消失，
 */
class EvaporateText : BaseText() {

    lateinit var animator: ValueAnimator
    var charTime = 300          // 进场动画，退场动画的理想时间
    var mostCount = 20          // 动画顺序效果的理想档数
    var duration: Long = 0      // 实际的动画时长

    var differentList: ArrayList<CharacterDiffResult> = arrayListOf()
    var textHeight: Int = 0

    override fun init(textView: BaseTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(textView, attrs, defStyle)
        animator = ValueAnimator()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : DefaultAnimatorListener(){
            override fun onAnimationEnd(animation: Animator) {
                listener?.onAnimationEnd(baseTextView)
            }
        })
        animator.addUpdateListener { animation ->
            progress = animation.getAnimatedValue() as Float
            baseTextView.invalidate()
        }
        val textLength = text?.length ?: 1
        // 退场动画时间 + 偏移的进场动画时间
        duration = (charTime + charTime / mostCount * (textLength - 1)).toLong()
    }

    override fun animateText(text: CharSequence) {
        baseTextView.post {
            oldStartX = baseTextView.layout.getLineLeft(0)
            super.animateText(text)
        }
    }

    override fun initVariables() {}

    override fun animateStart(text: CharSequence) {
        val length = text?.length ?: 1
        duration = (charTime + charTime / mostCount * (length - 1)).toLong()

        animator.cancel()
        animator.setFloatValues(0f, 1f)
        animator.duration = duration
        animator.start()
    }

    /* 在动画开启之前，获取绘制的计算基础 */
    override fun animatePrepare(text: CharSequence) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(this.oldText!!, text))

        val bounds = Rect()
        paint.getTextBounds(text.toString(), 0, text.length, bounds)
        textHeight = bounds.height()
    }

    override fun drawFrame(canvas: Canvas?) {
        val startX = baseTextView.layout.getLineLeft(0)
        val startY = baseTextView.baseline
        var offset = startX
        var oldOffset = oldStartX

        val maxLength = Math.max(text!!.length, oldText!!.length)
        for (i in 0 until maxLength) {
            // 绘制旧文本字符
            if (i < oldText!!.length) {
                var p = progress * duration / (charTime + charTime / mostCount * (text!!.length - 1))
                oldPaint.textSize = textSize
                val move = CharacterUtils.needMove(i, differentList)
                if (move != -1) {
                    oldPaint.alpha = 255
                    val pp = if (p * 2 > 1) 1f else p * 2
                    // 平移位置
                    val distX = CharacterUtils.getOffset(i, move, pp,
                            oldStartX.toInt(), startX.toInt(), oldGapList, gapList)
                    canvas?.drawText(oldText!!.get(i).toString(), 0, 1, distX, startY.toFloat(), oldPaint)
                } else {
                    oldPaint.alpha = ((1 - p) * 255).toInt()
                    val y = startY - p * textHeight                                 // 上移位置
                    val width = oldPaint.measureText(oldText!!.get(i).toString())   // 字符宽度
                    canvas?.drawText(oldText!!.get(i).toString(), 0, 1,
                            oldOffset + (oldGapList[i] - width) / 2, y, oldPaint )
                }
                oldOffset += oldGapList[i]
            }

            if (i < text!!.length) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    var alpha = (255f / charTime * (progress * duration - charTime * i / mostCount)).toInt()
                    alpha = if (alpha > 255) 255 else alpha
                    alpha = if (alpha < 0) 0 else alpha

                    paint.alpha = alpha
                    paint.textSize = textSize

                    var p = progress * duration / (charTime + charTime / mostCount * (text!!.length - 1))
                    val y = textHeight + startY - p * textHeight
                    val width = paint.measureText(text!!.get(i).toString())
                    canvas?.drawText(text!!.get(i).toString(), 0, 1,
                            offset + (gapList[i] - width) / 2, y, paint)
                }
                offset += gapList[i]
            }
        }
    }
}