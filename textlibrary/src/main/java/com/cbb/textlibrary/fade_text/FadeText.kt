package com.cbb.textlibrary.fade_text

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.cbb.textlibrary.R
import com.cbb.textlibrary.base.BaseText
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.base.DefaultAnimatorListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by CBB on 2018/4/2.
 * describe: 一个带渐淡切换的textView的绑定类
 */
class FadeText : BaseText() {

    val DEFAULT_DURATION = 2000
    var animationDuration: Int = 0
    private lateinit var random: Random
    private var alphaList: ArrayList<Int> = arrayListOf()        // 透明度集合

    override fun init(textView: BaseTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(textView, attrs, defStyle)
        val typedArray = textView.context.obtainStyledAttributes(attrs, R.styleable.fadeTextView)
        animationDuration = typedArray.getInt(R.styleable.fadeTextView_fade_animation_duration, DEFAULT_DURATION)
        typedArray.recycle()
    }

    /* 每次更新显示内容，重新设置透明度 */
    override fun initVariables() {
        random = Random()
        alphaList?.clear()
        for (i in 0 until baseTextView.text.length) {   // 遍历每个字符，随机设置3个档的透明度
            val randomNum = random.nextInt(2)    // 0 or 1
            if ((i + 1) % (randomNum + 2) == 0) {       // 2 or 3
                if ((i + 1) % (randomNum + 4) == 0) {   // 4 or 5
                    alphaList?.add(55)
                } else alphaList.add(255)
            } else {
                if ((i + 1) % (randomNum + 4) == 0) {   // 4 or 5
                    alphaList.add(55)
                } else alphaList.add(0)
            }
        }
    }

    /* 属性动画设置，并开启 */
    override fun animateStart(text: CharSequence) {
        initVariables()                 // 必须再次调用，否则会有索引越界的异常。
        val animator = ValueAnimator.ofFloat(0f, 1f)
                .setDuration(animationDuration.toLong())
        animator.interpolator = LinearInterpolator()

        // 结束的监听，及动画进行时的回调view绘制
        animator.addListener(object : DefaultAnimatorListener(){
            override fun onAnimationEnd(animation: Animator) {
                listener?.onAnimationEnd(baseTextView)
            }
        })
        animator.addUpdateListener { animation ->
            progress = animation.getAnimatedValue() as Float
            baseTextView.invalidate()
        }
        // 开始动画
        animator.start()
    }

    override fun animatePrepare(text: CharSequence) {
    }

    /* 绘制自定义的淡出效果 */
    override fun drawFrame(canvas: Canvas?) {
        val layout = baseTextView.layout
        var gapIndex = -1                            // 字符索引
        for (i in 0 until layout.lineCount) {       // 遍历文本的行数
            val lineStart = layout.getLineStart(i)  // 该行起始的pos
            val lineEnd = layout.getLineEnd(i)      // 该行结束的pos
            var lineLeft = layout.getLineLeft(i)    // 该行起始坐标
            val lineBaseLine = layout.getLineBaseline(i)    // 该行基线
            val lineText = text?.subSequence(lineStart, lineEnd).toString()
            for (c in 0 until lineText.length) {            // 该行的每个字符绘制透明度
                gapIndex ++
                val alpha = alphaList.get(gapIndex)
                paint.alpha = ((255 - alpha) * progress + alpha).toInt()
                canvas?.drawText(lineText[c].toString(), lineLeft, lineBaseLine.toFloat(), paint)
                lineLeft += gapList[gapIndex]             // 自增到下一个字符
            }
        }
    }
}