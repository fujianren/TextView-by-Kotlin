package com.cbb.textlibrary.line_text

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.cbb.textlibrary.R
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseText
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.base.DefaultAnimatorListener
import com.cbb.textlibrary.utils.DisplayUtils

/**
 * Created by CBB on 2018/3/30.
 * describe:
 */
class LineText : BaseText() {

    companion object {
        val DEFAULT_DURATION = 800                          // 默认动画时长
        val DEFAULT_LINE_WIDTH = DisplayUtils.dp2px(3f) // 默认外框线条宽度
    }
    /* 暴露的属性值 */
    var lineColor: Int = 0
    var lineWidth: Float = 0f
    var animationDuration: Int = 0

    lateinit var linePaint: Paint   // 外框线条的Paint
    /* 组成外框的点 */
    val p1 = PointF()
    val p2 = PointF()
    val p3 = PointF()
    val p4 = PointF()
    val p5 = PointF()
    val p6 = PointF()
    val p7 = PointF()
    val p8 = PointF()
    /* View的顶点 */
    val pA = PointF()
    val pB = PointF()
    val pC = PointF()
    val pD = PointF()


    /* 实现View的初始化 */
    override fun init(textView: BaseTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(textView, attrs, defStyle)

        val typedArray = textView.context.obtainStyledAttributes(attrs, R.styleable.lineTextView)
        lineColor = typedArray.getColor(R.styleable.lineTextView_lineColor, textView.currentTextColor)
        lineWidth = typedArray.getFloat(R.styleable.lineTextView_lineWidth, DEFAULT_LINE_WIDTH.toFloat())
        animationDuration = typedArray.getInt(R.styleable.lineTextView_animationDuration, DEFAULT_DURATION)
        typedArray.recycle()
    }

    /* 初始化动画绘制的相关变量，尺寸变化时，触发该方法 */
    override fun initVariables() {
        lineWidth = DEFAULT_LINE_WIDTH.toFloat()
        animationDuration = DEFAULT_DURATION

        linePaint = Paint()
        linePaint.color = lineColor
        linePaint.strokeWidth = lineWidth
    }

    /* 设置并开启动画 */
    override fun animateStart(text: CharSequence) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(animationDuration.toLong())
        valueAnimator.interpolator = LinearInterpolator()

        valueAnimator.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationEnd(animation: Animator) {
                if (listener != null){
                    listener!!.onAnimationEnd(baseTextView)
                }
            }
        })
        valueAnimator.addUpdateListener { animation ->
            progress = animation.getAnimatedValue() as Float
            baseTextView.invalidate()
        }
        valueAnimator.start()
    }

    override fun animatePrepare(text: CharSequence) {

    }

    /* 绘制text */
    override fun drawFrame(canvas: Canvas?) {
        val percent = progress          // 顺时针至空白处的线段比
        val percent2                    // 逆时针至空白处的线段比
                = Math.sqrt((3.38f - (percent - 1.7f) * (percent - 1.7f)).toDouble()) - 0.7f
        val width = baseTextView.width
        val height = baseTextView.height

        pA.x = 0f
        pA.y = 0f
        pB.x = width.toFloat()
        pB.y = 0f
        pC.x = width.toFloat()
        pC.y = height.toFloat()
        pD.x = 0f
        pD.y = height.toFloat()

        p1.x = (width * percent2).toFloat()
        p1.y = pB.y
        drawLine(canvas, p1, pB)

        p2.x = pB.x
        p2.y = height * percent
        drawLine(canvas, pB, p2)

        p3.x = width.toFloat()
        p3.y = (height * percent2).toFloat()
        drawLine(canvas, p3, pC)

        p4.x = width * (1 - percent)
        p4.y = height.toFloat()
        drawLine(canvas, pC, p4)

        p5.x = width * (1 - percent)
        p5.y = height.toFloat()
        drawLine(canvas, p5, pD)

        p6.x = 0f
        p6.y = height * (1 - percent)
        drawLine(canvas,pD, p6)

        p7.x = 0f
        p7.y = (height * (1 - percent2)).toFloat()
        drawLine(canvas, p7, pA)

        p8.x = width * percent
        p8.y = 0f
        drawLine(canvas, pA, p8)
    }

    private fun drawLine(canvas: Canvas?, a: PointF, b: PointF) {
        canvas?.drawLine(a.x, a.y, b.x, b.y, linePaint)
    }
}