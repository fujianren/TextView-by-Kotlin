package com.cbb.textlibrary.fall_text

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
class FallTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : BaseTextView(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    var fall_Text: Fall_Text
    init {
        fall_Text = Fall_Text()
        fall_Text.init(this, attrs, defStyleAttr)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun setAnimationListener(listener: AnimationListener) {
        fall_Text.setAnimationListener(listener)
    }

    override fun setProgress(process: Float) {
        fall_Text.progress = process
    }

    override fun animateText(text: CharSequence) {
        fall_Text.animateText(text)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        fall_Text.onDraw(canvas)
    }
}