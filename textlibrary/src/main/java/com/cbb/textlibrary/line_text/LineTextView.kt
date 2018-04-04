package com.cbb.textlibrary.line_text

import android.content.Context
import android.graphics.Canvas
import android.support.annotation.ColorInt
import android.util.AttributeSet
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseTextView

/**
 * Created by CBB on 2018/3/30.
 * describe: 跑马灯外框的TextView
 * 主要功能代码由绑定的LineText完成，View本身仅提供流程
 */
class LineTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : BaseTextView(context, attrs, defStyleAttr) {

    /* 绑定的text */
    lateinit var lineText: LineText

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        lineText = LineText()
        lineText.init(this, attrs, defStyleAttr)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lineText.onDraw(canvas)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        lineText.setAnimationListener(listener)
    }

    override fun setProgress(process: Float) {
        lineText.progress = process
    }

    override fun animateText(text: CharSequence) {
        lineText.animateText(text)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 暴露的属性设置
    ///////////////////////////////////////////////////////////////////////////
    fun setLineColor(@ColorInt color: Int) {
        lineText.lineColor = color
    }

    fun getLineWidth(): Float {
        return lineText.lineWidth
    }

    fun setLineWidth(lineWidth: Float) {
        lineText.lineWidth = lineWidth
    }

    fun getAnimationDuration() : Int{
        return lineText.animationDuration
    }

    fun setAnimationDuration(duration: Int) {
        lineText.animationDuration = duration
    }

}