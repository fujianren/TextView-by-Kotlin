package com.cbb.textlibrary

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by CBB on 2018/3/29.
 * describe: 一个颜色变化的TextView,实现闪烁的效果，只提供了单色变化
 * 1、利用属性动画，在监听器中不断回调onDraw刷新
 * 2、利用矩阵，将动画进度值，作用给着色器，使着色器变化，并将变化后的着色器设置给Paint
 */
class LightTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context?): this(context, null)

    /* 原生画笔 */
    lateinit var paint: Paint
    /* 循环动画 */
    lateinit var animator: ValueAnimator
    /* 动画进度偏移值 */
    var dx: Float = 0f
    /* 线性渐变色对象，水平，拉伸 */
    lateinit var linearGradient: LinearGradient
    /* 闪烁的颜色，必须是16进制 */
    var lightColor = 0xff00ff00

    fun setLightColor(colorInt: Int){
        lightColor = colorInt.toLong()
        invalidate()
    }

    init {
        paint = getPaint();         // TextView原生的paint
    }

    /**
     * 每次布局时，开启一个循环属性动画,一个线性着色器
     */
    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        animator = ValueAnimator.ofFloat(0f, (2 * measuredWidth).toFloat())
        animator.addUpdateListener { animation ->
            dx = animation.animatedValue as Float
            postInvalidate()
        }
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.duration = 2000
        animator.start()

        linearGradient = LinearGradient(-measuredWidth.toFloat(), 0f,
                0f, 0f,
                intArrayOf(currentTextColor, lightColor.toInt(), currentTextColor),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP)
    }


    /**
     * 在绘制之前，自定义画笔的着色
     */
    override fun onDraw(canvas: Canvas?) {
        // 将动画进度的变化作用到着色器，再将着色器配置给Paint
        val matrix = Matrix()
        matrix.setTranslate(dx, 0f)
        if (linearGradient != null){
            linearGradient.setLocalMatrix(matrix)
            paint.setShader(linearGradient)
        }
        super.onDraw(canvas)
    }


}