package com.cbb.textlibrary.utils

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Created by CBB on 2018/3/30.
 * describe: 屏幕分辨率转换
 */
object DisplayUtils {

    fun getDisplayMetrics() : DisplayMetrics{
        return Resources.getSystem().displayMetrics
    }

    fun dp2px(dp: Float): Int {
        return Math.round(dp * getDisplayMetrics().density)
    }

    fun getScreenWidth(): Int {
        return getDisplayMetrics().widthPixels
    }

    fun getScreenHeight(): Int {
        return getDisplayMetrics().heightPixels
    }
}