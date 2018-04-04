package com.cbb.textlibrary.base

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by CBB on 2018/3/30.
 * describe: 抽象的TextView的基类
 * 主要抽取3个方法
 * 1、添加自定义的动画监听
 * 2、动画进度设置
 * 3、显示text，带动画效果
 */
abstract class BaseTextView(context: Context?, attrs: AttributeSet?,  defStyleAttr: Int)
    : TextView(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    abstract fun setAnimationListener(listener: AnimationListener)

    abstract fun setProgress(process: Float)

    /**
     * text显示的方法，同时触发动画
     **/
    abstract fun animateText(text: CharSequence)
}