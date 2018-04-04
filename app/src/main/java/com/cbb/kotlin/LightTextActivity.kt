package com.cbb.kotlin

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cbb.textlibrary.LightTextView
import com.cbb.textlibrary.base.AnimationListener
import com.cbb.textlibrary.base.BaseTextView
import com.cbb.textlibrary.evaporate_text.EvaporateTextView
import com.cbb.textlibrary.fade_text.FadeTextView
import com.cbb.textlibrary.fall_text.FallTextView
import com.cbb.textlibrary.line_text.LineTextView
import com.cbb.textlibrary.scale_text.ScaleTextView
import com.cbb.textlibrary.scroll_sides_text.ScrollSideTextView

class LightTextActivity : AppCompatActivity() {

    internal var sentences = arrayOf("What is design?", "Design is not just", "what it looks like and feels like.", "Design is how it works. \n- Steve Jobs", "Older people", "sit down and ask,", "'What is it?'", "but the boy asks,", "'What can I do with it?'. \n- Steve Jobs", "Swift", "Objective-C", "iPhone", "iPad", "Mac Mini", "MacBook Pro", "Mac Pro", "爱老婆", "老婆和女儿")
    internal var index: Int = 0
    val string: String = "高堂明镜悲白发，朝如青丝暮成霜"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_text)

        val textView = findViewById<LightTextView>(R.id.text)
        textView.setText(string)
        textView.setLightColor(0xffff0000.toInt())

        val lineTextView = findViewById<LineTextView>(R.id.text4)
        lineTextView.setLineColor(Color.RED)
        lineTextView.animateText(string)
        lineTextView.setOnClickListener(ClickListener())
        lineTextView.setAnimationListener(SimpleAnimationListener(this))

        val fadeTextView = findViewById<FadeTextView>(R.id.text_fade)
        fadeTextView.setTextColor(Color.parseColor("#ffff0000"))
        fadeTextView.animateText(string)
        fadeTextView.setAnimationDuration(1000)
        fadeTextView.setOnClickListener(ClickListener())
        fadeTextView.setAnimationListener(SimpleAnimationListener(this))

        val scrollTextView = findViewById<ScrollSideTextView>(R.id.text_scroll)
        scrollTextView.setOnClickListener(ClickListener())
        scrollTextView.setAnimationListener(SimpleAnimationListener(this))

        val scaleTextView = findViewById<ScaleTextView>(R.id.text_scale)
        scaleTextView.setOnClickListener(ClickListener())
        scaleTextView.setAnimationListener(SimpleAnimationListener(this))

        val evaporateTextView = findViewById<EvaporateTextView>(R.id.text_evaporate)
        evaporateTextView.animateText(string)
        evaporateTextView.setOnClickListener(ClickListener())
//        evaporateTextView.setAnimationListener(SimpleAnimationListener(this))

        val fallTextView = findViewById<FallTextView>(R.id.text_fall)
        fallTextView.animateText(string)
        fallTextView.setOnClickListener(ClickListener())
        fallTextView.setAnimationListener(SimpleAnimationListener(this))
    }


    internal inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            if (v is BaseTextView) {
                if (index + 1 >= sentences.size) {
                    index = 0
                }
                v.animateText(sentences[index++])
            }
        }
    }

    class SimpleAnimationListener(private val context: Context) : AnimationListener {
        override fun onAnimationEnd(hTextView: BaseTextView) {
            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show()
        }
    }


}
