package com.cbb.textlibrary.evaporate_text

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseTextView

/**
 * Created by CBB on 2018/4/3.
 * describe:
 */
class EvaporateTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : BaseTextView(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    lateinit var evaporateText: EvaporateText
    init {
        evaporateText = EvaporateText()
        evaporateText.init(this, attrs, defStyleAttr)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END        // 末尾溢出省略号表示
    }

    override fun setAnimationListener(listener: AnimationListener) {
        evaporateText.setAnimationListener(listener)
    }

    override fun setProgress(process: Float) {
        evaporateText.progress = process
    }

    override fun animateText(text: CharSequence) {
        evaporateText.animateText(text)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)  // 注释之后，padding之类的需要自己测量
        evaporateText.onDraw(canvas)
    }
}