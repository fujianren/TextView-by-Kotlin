package com.cbb.textlibrary.base

import android.view.ViewTreeObserver

/**
 * Created by CBB on 2018/3/30.
 * describe: 动画TextView的动画结速监听的接口
 */
interface AnimationListener{

    fun onAnimationEnd(textView: BaseTextView)
}