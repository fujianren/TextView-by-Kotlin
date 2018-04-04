package com.cbb.textlibrary.scale_text

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.cbb.textlibrary.base.BaseText
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.base.DefaultAnimatorListener
import com.cbb.textlibrary.utils.CharacterDiffResult
import com.cbb.textlibrary.utils.CharacterUtils

/**
 * Created by CBB on 2018/4/2.
 * describe: 切换文本时，新字符会逐渐放大，旧字符会移动的TextView
 * 技术点：
 * 1、给动画进度与动画时间的自定义关系，以满足各字符可以依据进度显示出不同的效果
 * 2、根据动画进度的自定义关系，读取出每个字符的瞬间显示状态
 * 3、依次绘制出每个字符
 */
class ScaleText : BaseText() {

    var differentList = arrayListOf<CharacterDiffResult>()
    lateinit var animator: ValueAnimator
    var charTime = 400                  // 给予字符切换替换时的动画时间，值越大，动画变化的越慢，
    var mostCount = 20                  // 新字符出现时的默认最大数量，值越大，动画变化的越快
    var duration: Long = 0              // 整体动画时长，旧文本字符消失的charTime+新文本出现的charTime，

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
        var length = text?.length ?: 0
        length = if (length <= 0) 1 else length
        duration = (charTime + charTime / mostCount * (length - 1)).toLong()
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
        var n = text.length
        n = if (n <= 0) 1 else n
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()
        animator.cancel()
        animator.setFloatValues(0f, 1f)
        animator.duration = duration
        animator.start()
    }

    /* 动画前，重置差异字符 */
    override fun animatePrepare(text: CharSequence) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(oldText!!, this.text!!))
    }

    override fun drawFrame(canvas: Canvas?) {
        if (canvas != null){
            val startX = baseTextView.layout.getLineLeft(0)     // 文本起点
            val startY = baseTextView.layout.getLineBaseline(0) // 文本基线
            var offset = startX
            var oldOffset = oldStartX                               // 旧文本开始绘制的偏移量
            val oldLength = oldText?.length ?: 0
            val newLength = text?.length ?: 0
            var maxLength = Math.max(newLength, oldLength)
            for (i in 0 until maxLength) {                              // 遍历
                if (i < oldLength) {                                    // 该位置的字符是不是会出现在下次
                    val move = CharacterUtils.needMove(i, differentList)// 字符下次的出现位置
                    if (move != -1) {                                   // 若该字符下次会再出现
                        oldPaint.textSize = textSize!!                  // 则大小透明不变，位置随变化
                        oldPaint.alpha = 255
                        var p = progress * 2
                        p = if (p > 1) 1F else p
                        val distX = CharacterUtils.getOffset(i, move, p, oldStartX.toInt(), startX.toInt(), oldGapList, gapList)
                        canvas?.drawText(oldText?.get(i).toString(), 0, 1, distX, startY.toFloat(), oldPaint)    // 指定位置绘制该字符
                    } else {                                            // 若该字符下次不会出现
                        oldPaint.alpha = ((1 - progress) * 255).toInt() // 则大小透明随变，位置不变
                        oldPaint.textSize = textSize!! * (1 - progress)
                        val width = oldPaint.measureText(oldText?.get(i).toString())
                        canvas.drawText(oldText?.get(i).toString(),
                                0, 1,
                                oldOffset + (oldGapList.get(i) - width) / 2, startY.toFloat(), oldPaint)
                    }
                    oldOffset += oldGapList.get(i)
                }

                if (i < newLength) {
                    if (!CharacterUtils.stayHere(i, differentList)) {   // 该位置上的字符是新生成的
                        // 新字符出现时，利用已耗时与新字符动画总时长charTime的比值，确定每个新字符的动画完成情况
                        // 计算的时候一定要给255带单位，不然会出现奇葩的错误
                        var alpha = (255f / charTime * (progress * duration - charTime * i / mostCount)).toInt()
                        if (alpha > 255) alpha = 255
                        if (alpha < 0) alpha = 0

                        var size = textSize * 1f / charTime * (progress * duration - charTime * i / mostCount)
                        if (size > textSize) size = textSize
                        if (size < 0) size = 0f

                        paint.alpha = alpha
                        paint.textSize = size

                        val width = paint.measureText(text?.get(i).toString())
                        canvas.drawText(text?.get(i).toString(), 0, 1,
                                offset +  (gapList.get(i) - width) / 2, startY.toFloat() ,paint)
                    }
                    offset += gapList.get(i)   // 新字符绘制位置依次后移
                }
            }
        }
    }
}