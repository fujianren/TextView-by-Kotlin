package com.cbb.textlibrary.base

import android.animation.Animator

/**
 * Created by CBB on 2018/3/30.
 * describe: 仅仅只是为了减少代码量的类
 */
open class DefaultAnimatorListener : Animator.AnimatorListener{
    override fun onAnimationRepeat(animation: Animator) {}

    override fun onAnimationEnd(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator) {}

    override fun onAnimationStart(animation: Animator) {}
}