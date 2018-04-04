package com.cbb.textlibrary.fade_text

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseTextView

/**
 * Created by CBB on 2018/4/2.
 * describe:文本变化时，文字有暗淡效果的TextView
 */
class FadeTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : BaseTextView(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    var fadeText: FadeText = FadeText()

    init {
        fadeText.init(this, attrs, defStyleAttr)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        fadeText.setAnimationListener(listener)
    }

    override fun setProgress(process: Float) {
        fadeText.progress = process
    }

    override fun animateText(text: CharSequence) {
        fadeText.animateText(text)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)      // 要注释掉，我们自己重写了文本的绘制.但是其他原有绘制的布局也会无效掉
        fadeText.onDraw(canvas)
    }

    fun setAnimationDuration(duration: Int) {
        fadeText.animationDuration = duration
    }

    fun getAnimationDuration(): Int = fadeText.animationDuration
}