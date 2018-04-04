package com.cbb.textlibrary.base

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver

/**
 * Created by CBB on 2018/3/30.
 * describe: 配合BaseTextView使用的特殊text
 * 该text封装了动画
 */
abstract class BaseText : IBaseText, ViewTreeObserver.OnGlobalLayoutListener {
    
    lateinit var baseTextView: BaseTextView // 自定义的TextView
    var oldText: CharSequence? = null       // 切换动画之前的文本
    var text: CharSequence? = null             // 本次要显示的文本
    var textSize: Float = 10f
    var height: Int? = null
    var width: Int? = null

    lateinit var oldPaint: Paint
    lateinit var paint: Paint

    var oldStartX: Float = 0f
    var progress: Float = 0f                // 属性动画的进度，0~1
    var listener: AnimationListener? = null

    var oldGapList = arrayListOf<Float>()   // 旧文本的各字符测量值
    var gapList = arrayListOf<Float>()      // 新文本的各字符测量值

    /* 基本的绘制材料准备,动画状态记录 */
    override fun init(textView: BaseTextView, attrs: AttributeSet?, defStyle: Int) {
        baseTextView = textView
        oldText = ""
        text = textView.text
        progress = 1f

        paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        oldPaint = TextPaint(paint)

        // 通过视图监听，获取view的展示尺寸
        baseTextView!!.viewTreeObserver.addOnGlobalLayoutListener(this)
        prepareAnimate()
    }

    override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            baseTextView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
        } else {
            baseTextView!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
        }
        textSize = baseTextView!!.textSize
        width = baseTextView.width
        height = baseTextView.height
        oldStartX = baseTextView.layout!!.getLineLeft(0)

        initVariables()
    }


    /* 记录动画前状态,动画后状态 */
    private fun prepareAnimate() {
        textSize = baseTextView.textSize
        paint.textSize = textSize as Float
        paint.color = baseTextView.currentTextColor
        paint.typeface = baseTextView.typeface
        gapList.clear()
        val n = text?.length ?: 0
        for (i in 0 until n){      // 获取每个字符的尺寸，保存
            gapList.add(paint.measureText(text!!.get(i).toString()))
        }
        oldPaint.textSize = textSize as Float
        oldPaint.color = baseTextView.currentTextColor
        oldPaint.typeface = baseTextView.typeface
        oldGapList.clear()
        for (i in oldText!!.indices){
            oldGapList.add(oldPaint.measureText(oldText!!.get(i).toString()))
        }
    }

    /* 准备text的动画，并开启 */
    override fun animateText(text: CharSequence) {
        // 动画前后，状态替换
        baseTextView.setText(text)
        oldText = this.text
        this.text = text
        prepareAnimate()
        animatePrepare(text)
        animateStart(text)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        this.listener = listener
    }

    override fun onDraw(canvas: Canvas?) {
        drawFrame(canvas)
    }

    /* TextView尺寸变化时，触发该方法 */
    abstract fun initVariables()

    /* 开启text的动画 */
    abstract fun animateStart(text: CharSequence)

    /* 动画前，设置text */
    abstract fun animatePrepare(text: CharSequence)

    /* view的绘制触发该方法，手动绘制动画效果 */
    abstract fun drawFrame(canvas: Canvas?)

}

