package com.cbb.textlibrary.fall_text

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.cbb.textlibrary.base.BaseText
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.base.DefaultAnimatorListener
import com.cbb.textlibrary.utils.CharacterDiffResult
import com.cbb.textlibrary.utils.CharacterUtils
import kotlin.math.abs

/**
 * Created by CBB on 2018/4/3.
 * describe: 切换文本时，旧文本翻转落下，新文本放大升起
 */
class Fall_Text : BaseText() {

    lateinit var animator: ValueAnimator
    var charTime = 400
    var mostCount = 20
    var duration: Long = 0
    var differentList: ArrayList<CharacterDiffResult> = arrayListOf()
    var textHeight = 0

    val interpolator: OvershootInterpolator = OvershootInterpolator()

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
        val n = text?.length ?: 1
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()
    }

    override fun animateText(text: CharSequence) {
        baseTextView.post {
            oldStartX = baseTextView.layout.getLineLeft(0)
            super.animateText(text)
        }
    }

    override fun initVariables() {

    }

    override fun animateStart(text: CharSequence) {
        val n = text.length
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()
        animator.cancel()
        animator.setFloatValues(0f, 1f)
        animator.duration = duration
        animator.start()
    }

    override fun animatePrepare(text: CharSequence) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(this.oldText!!, text))
        val bounds = Rect()
        paint.getTextBounds(text.toString(), 0, text.length, bounds)
        textHeight = bounds.height()
    }

    override fun drawFrame(canvas: Canvas?) {
        val startX = baseTextView.layout.getLineLeft(0)
        val startY = baseTextView.baseline.toFloat()
        var offset = startX
        var oldOffset = oldStartX
        val maxLength = Math.max(text!!.length, oldText!!.length)

        for (i in 0 until maxLength) {
            if (i < oldText!!.length) {
                var p = progress * duration / (charTime + charTime / mostCount * (text!!.length - 1))

                oldPaint.textSize = textSize
                val move = CharacterUtils.needMove(i, differentList)
                if (move != -1) {           // 平移的字符
                    oldPaint.alpha = 255
                    val pp = if (p * 2 > 1) 1f else p * 2
                    val distX = CharacterUtils.getOffset(i, move, pp,
                            oldStartX.toInt(), startX.toInt(), oldGapList, gapList)
                    canvas?.drawText(oldText!!.get(i).toString(), 0, 1,
                            distX, startY, oldPaint)
                } else {                    // 摇摆之后，垂直下落的字符
                    oldPaint.alpha = 255
                    val centerX = oldOffset + oldGapList.get(i) / 2
                    val width = oldPaint.measureText(oldText?.get(i).toString())

                    var pp = if (p * 1.4 > 1) 1f else p * 1.4f
                    pp = interpolator.getInterpolation(pp)
                    var angle = (1 - pp) * Math.PI
                    if (i % 2 == 0) {                           // 相邻左右摇摆
                        angle = (p * Math.PI) + Math.PI
                    }
                    val disX: Float = (centerX + (width / 2 * Math.cos(angle))).toFloat()
                    val disY: Float = (startY + (width / 2 * Math.sin(angle))).toFloat()
                    oldPaint.style = Paint.Style.STROKE

                    var path = Path()
                    path.moveTo(disX, disY)
                    path.lineTo(2 * centerX - disX, 2 * startY - disY)
                    if (p <= 0.7) {                       // 摇摆
                        canvas?.drawTextOnPath(oldText!!.get(i).toString(), path, 0f, 0f, oldPaint)
                    } else {                              // 下落
                        val p2 = (p - 0.7) / 0.3
                        oldPaint.alpha = ((1 - p2) * 255).toInt()
                        val y = textHeight * 2f
                        val path2 = Path()
                        path2.moveTo(disX, disY + y)
                        path2.lineTo(2 * centerX - disX, 2 * startY - disY + y)
                        canvas?.drawTextOnPath(oldText!!.get(i).toString(), path2, 0f, 0f, oldPaint)
                    }
                }
                oldOffset += oldGapList[i]
            }

            if (i < text!!.length) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    var alpha = (255f / charTime * (progress * duration - charTime * i / mostCount)).toInt()
                    alpha = if (alpha > 255) 255 else alpha
                    alpha = if (alpha < 0) 0 else alpha

                    var size = textSize / charTime * (progress * duration - charTime * i / mostCount)
                    size = if (size > textSize) textSize else size
                    size = if (size < 0) 0f else size

                    paint.alpha = alpha
                    paint.textSize = size
                    val width = paint.measureText(text!!.get(i).toString())
                    canvas?.drawText(text!!.get(i).toString(), 0, 1,
                            offset + (gapList.get(i) - width) / 2, startY, paint)
                }
                offset += gapList[i]
            }
        }

    }
}