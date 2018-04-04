package com.cbb.kotlin.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.disklrucache.DiskLruCache
import java.io.Closeable
import java.security.MessageDigest

/**
 * Created by CBB on 2018/3/29.
 * describe: 硬盘缓存工具类
 */
object DiskLruCacheTools {
    lateinit var diskLruCache: DiskLruCache
    val maxSize: Int = 8 * 1024 * 1024

    /**
     * 初始化硬盘缓存对象
     */
    fun init(context: Context) {
        diskLruCache = DiskLruCache.open(context.cacheDir,
                getSystemVersionCode(context),1,maxSize.toLong())
    }

    /**
     * 从硬盘读取数据
     * @param path 数据路径
     */
    fun getBitmapFromDisk(path: String?): Bitmap {
        var key: String = getCacheKey(path)
        // key的正则为[a-z0-9_-]{1,64}
        val snapshot = diskLruCache.get(key)
        val inputStream = snapshot?.getInputStream(0)    // 输入到内存
        val bitmap = BitmapFactory.decodeStream(inputStream)
        close(inputStream)
        return bitmap
    }

    /**
     * 将数据保存到硬盘中
     * @param path 硬盘路径
     * @param bitmap 要存储的数据
     */
    fun putBitmapToDisk(path: String, bitmap: Bitmap){
        // 路径加密
        val key = getCacheKey(path)
        // 开启输出流
        val edit = diskLruCache.edit(key)
        val outputStream = edit.newOutputStream(0)
        // 写入并压缩数据
        val compress = bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
        if (compress) edit.commit()
        else edit.abort()
        // 关流
        close(outputStream)
    }


    fun close(closeable: Closeable?){
        closeable?.close() ?: return
    }


    /**
     * 加密路径
     */
    private fun getCacheKey(path: String?): String {
        if (path == null) return ""
        var md: MessageDigest = MessageDigest.getInstance("MD5")
        md.update(path.toByte())
        val bytes = md.digest()
        var strBuffer = StringBuffer()
        for (i in bytes.indices){
            strBuffer.append(Integer.toHexString(Math.abs(bytes[i].toInt())))
        }
        return strBuffer.toString()
    }

    /**
     * 获取app的版本号
     */
    private fun getSystemVersionCode(context: Context): Int {
        var packageInfo: PackageInfo
                = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionCode
    }
}