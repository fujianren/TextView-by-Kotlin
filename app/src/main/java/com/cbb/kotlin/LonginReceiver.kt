package com.cbb.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * Created by ZDS-T on 2018/3/5.
 * 广播接收器
 * 没有用户界面，但是可以启动一个activity来响应收到的信息
 * 或用NotificationManager来通知用户
 *
 */
public class LonginReceiver : BroadcastReceiver {

    constructor(){
        Log.d("dddd", "--------------")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("dddd", "=================")
        if (intent?.action.equals("action_login")) {
            val data = intent?.getStringExtra("data")
            Toast.makeText(context, data + "/cast", Toast.LENGTH_SHORT).show()
        }
    }
}