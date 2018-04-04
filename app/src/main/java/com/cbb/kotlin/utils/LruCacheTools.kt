package com.cbb.kotlin.utils

import android.graphics.Bitmap
import android.support.v4.util.LruCache


/**
 * Created by CBB on 2018/3/29.
 * describe: 内存读写工具类，LRU缓存原则
 */
object LruCacheTools {
    lateinit var lruCache: LruCache<String, Bitmap>
    var maxSize: Int = 8 * 1024 * 1024      // 内存分配最大8M

    init {
        lruCache = object: LruCache<String, Bitmap>(maxSize){
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value?.byteCount ?: 0
            }
        }
    }

    fun putBitmap(key: String, bitmap: Bitmap) {
        lruCache.put(key, bitmap)
    }

    fun getBitmap(key: String): Bitmap {
        return lruCache.get(key)
    }

}