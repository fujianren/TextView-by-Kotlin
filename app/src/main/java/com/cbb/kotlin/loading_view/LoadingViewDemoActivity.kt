package com.cbb.kotlin.loading_view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.cbb.kotlin.R


class LoadingViewDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_view_demo)

        val loadingView = findViewById<LoadingView>(R.id.loading_view)
        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnEnd = findViewById<Button>(R.id.btn_end)

        btnStart.setOnClickListener({ v: View? ->
            loadingView.start()
        })

        btnEnd.setOnClickListener({ v: View? ->
            loadingView.end()
        })
    }
}
