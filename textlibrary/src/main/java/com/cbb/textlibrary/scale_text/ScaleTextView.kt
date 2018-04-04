package com.cbb.textlibrary.scale_text

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
class ScaleTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : BaseTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    var scaleText: ScaleText
    init {
        scaleText = ScaleText()
        scaleText.init(this, attrs, defStyleAttr)
        maxLines = 1                            // 最多一行
        ellipsize = TextUtils.TruncateAt.END    // 多余的数据...省略
    }

    override fun setAnimationListener(listener: AnimationListener) {
        scaleText.setAnimationListener(listener)
    }

    override fun setProgress(process: Float) {
        scaleText.progress = process
    }

    override fun animateText(text: CharSequence) {
        scaleText.animateText(text)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        scaleText.onDraw(canvas)
    }
}