package com.cbb.textlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by CBB on 2018/3/29.
 * describe: 自定义图片居中的TextView
 * 原TextView中drawable默认绘制在边界处
 * 故在TextView绘制前，平移画布，消除边界和text之间的空隙
 * 使用注意，和Gravity属性中的某些设置有冲突，特别是设置center
 */
class DrawableCenterTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {

    /**
     * 在绘制之前，处理drawable的间距
     */
    override fun onDraw(canvas: Canvas) {
        var drawables = compoundDrawables
        if (drawables != null){     // 图在左
            var drawable = drawables[0]
            if (drawable != null){
                val textWidth = paint.measureText(text.toString())  // 文字长
                val drawablePadding = compoundDrawablePadding       // 图距
                val drawableWidth = drawable.intrinsicWidth         // 图宽
                val bodyWidth = textWidth + drawablePadding + drawableWidth
                canvas.translate((width - bodyWidth) / 2, 0f)
            } else if (drawables[1] != null){       // 图在上
                drawable = drawables[1]
                var rect = Rect()
                paint.getTextBounds(text.toString(), 0, text.toString().length, rect)
                val textHeight = rect.height()
                val drawablePadding = compoundDrawablePadding
                val drawableHeight = drawable.intrinsicHeight
                val bodyHeight = textHeight + drawablePadding + drawableHeight
                canvas.translate(0f, ((height - bodyHeight) / 2).toFloat())
            }
        }
        super.onDraw(canvas)
    }
}