package com.cbb.textlibrary.base

import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Created by CBB on 2018/3/30.
 * describe: BaseTextView使用的特殊文本
 * 提供4个必须实现的特性
 *
 */
interface IBaseText{

    /* 响应BaseTextView的初始化，将由该方法具体实现 */
    fun init(textView: BaseTextView, attrs: AttributeSet?, defStyle: Int)

    /* text设置并开启动画 */
    fun animateText(text: CharSequence)

    /* 响应BaseTextView的绘制，调用该方法 */
    fun onDraw(canvas: Canvas?)

    /* 响应Textview,为text设置一个自定义的监听器，监听动画结束 */
    fun setAnimationListener(listener: AnimationListener)
}