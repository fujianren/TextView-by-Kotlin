package com.cbb.kotlin.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView

/**
 * Created by CBB on 2018/3/29.
 * describe:
 */
class ImageLoader{

    /**
     * 加载图片
     * @param context
     * @param iv    显示图片的控件
     * @param path  图片的网络地址
     */
    fun loadImage(context: Context, iv: ImageView, path: String) {
        val bitmap = LruCacheTools.getBitmap(path)      // find in memory
        if (bitmap != null){    // show direct
            println("has found in memory")
            iv.setImageBitmap(bitmap)
        } else {                // find in disk
            println("not in memory, then find in disk")
            DiskAsynTask(context, iv).execute(path)
        }
    }

    /**
     * 异步线程,硬盘读取
     */
    class DiskAsynTask(var context: Context, var imageView: ImageView): AsyncTask<String, Void, Bitmap>() {
        lateinit var path: String       // 需要执行的路径

        /* 异步执行的操作 */
        override fun doInBackground(vararg params: String): Bitmap {
            path = params[0]
            return DiskLruCacheTools.getBitmapFromDisk(path)
        }

        /* 返回主线程的结果 */
        override fun onPostExecute(result: Bitmap?) {
            if (result != null){
                println("has found in disk")
                imageView.setImageBitmap(result)
                LruCacheTools.putBitmap(path, result)
            } else {
                println("not in disk, should download from net")
                DownLoadImageAsynTask(context, imageView).execute(path)
            }

        }
    }

    /**
     * 异步线程，网络下载
     */
    class DownLoadImageAsynTask(var context: Context, var iv: ImageView): AsyncTask<String, Void, Bitmap>(){
        var path: String = ""
        override fun doInBackground(vararg params: String): Bitmap {
            path = params[0]
            // 开启网络请求，获取图片
//            HttpUtils.getBitmapWithPaht(path)
            return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_4444);
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null){        // 下载成功
                DiskLruCacheTools.putBitmapToDisk(path, result)     // 存入硬盘
                LruCacheTools.putBitmap(path, result)               // 存入内存
                iv.setImageBitmap(result)                           // 控件显示
            } else {
                println("URL is not exist")
            }
        }

    }
}