package com.cbb.textlibrary.scroll_sides_text

import android.content.Context
import android.os.Handler
import android.os.Handler.Callback
import android.os.Message
import android.util.AttributeSet
import com.cbb.textlibrary.R
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseTextView
import java.util.*


/**
 * Created by CBB on 2018/4/2.
 * describe: 可以像卷轴一样左右展开的TextView
 * 利用handler机制的实现效果
 * 初始化时，handler发送消息，
 * 收到消息，开始拼接固定字符，触发View的绘制，并发送下一次的消息
 * 字符拼接完整，不再发送消息
 */
class ScrollSideTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : BaseTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    companion object {
        val INVALIDATE = 0x767
    }

    var speed: Int
    var charIncrease: Int               // 每次展开增加的字符数
    var random: Random
    var wholeText: CharSequence         // 完整的text
    lateinit var myHandler: Handler     // 充当定时器
    var myListener: AnimationListener? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.scrollSideTextView)
        speed = typedArray.getInt(R.styleable.scrollSideTextView_expandSpeed, 100)
        charIncrease = typedArray.getInt(R.styleable.scrollSideTextView_charIncrease, 2)
        typedArray.recycle()

        wholeText = text
        random = Random()

        myHandler = Handler(Callback { msg: Message? ->
            val currentlength = text.length
            if (currentlength < wholeText.length) {
                if (currentlength + charIncrease > wholeText.length) {
                    charIncrease = wholeText.length - currentlength
                }
                append(wholeText.subSequence(currentlength, currentlength + charIncrease))  // 拼接触发绘制

                val randomTime = speed + random.nextInt(speed)
                val message = Message.obtain()
                message.what = INVALIDATE
                myHandler.sendMessageDelayed(message, randomTime.toLong())                  // 下一次绘制
            } else {
                myListener?.onAnimationEnd(this)
            }
            return@Callback false
        })

    }


    override fun setAnimationListener(listener: AnimationListener) {
        myListener = listener
    }

    override fun setProgress(process: Float) {
        setText(wholeText.subSequence(0, (wholeText.length * process).toInt()))
    }

    override fun animateText(text: CharSequence) {
        if (text == null) throw RuntimeException("text must not be null")
        wholeText = text
        setText("")
        val message = Message.obtain()
        message.what = INVALIDATE
        myHandler.sendMessage(message)
    }

    /* 销毁View时，同时销毁handler */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        myHandler.removeMessages(INVALIDATE)
    }
}