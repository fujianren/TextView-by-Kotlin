package com.cbb.kotlin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cbb.kotlin.loading_view.LoadingView;
import com.cbb.kotlin.loading_view.LoadingViewDemoActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private LinearLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (LinearLayout) findViewById(R.id.container);

        add("六边形闪烁加载", LoadingViewDemoActivity.class);
        add("闪烁的文本", LightTextActivity.class);
    }

    private void add(String name, Class clazz) {
        Button button = new Button(this);
        button.setText(name);
        button.setPadding(5, 5, 5, 5);
        ViewGroup.LayoutParams params
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, clazz);
                startActivity(intent);
            }
        });

        mLayout.addView(button);
    }

    // 向广播接收器发送意图
    private void startReceiver() {
        Intent intent = new Intent();
        intent.setAction("com.cbb.kotlin");
        intent.putExtra("data", "data");
        sendBroadcast(intent);
    }

    // 动态注册
    private void regiterRectiver(){
        LonginReceiver receiver = new LonginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.cbb.kotlin");
        registerReceiver(receiver, filter);
    }

}
