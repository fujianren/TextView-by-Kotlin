package com.cbb.textlibrary.utils

import android.util.Log
import com.cbb.textlibrary.base.IBaseText

/**
 * Created by CBB on 2018/3/30.
 * describe:
 */
object CharacterUtils {

    /**
     *
     * @param oldText   旧字符串
     * @param newText   新字符串
     * @return 返回一个集合，记录旧字符串字符在新字符串上的位置
     */
    fun diff(oldText: CharSequence, newText: CharSequence) : List<CharacterDiffResult>{
        val differentList = arrayListOf<CharacterDiffResult>()

        val skip = hashSetOf<Int>()
        for (i in 0 until oldText.length){
            val c = oldText.get(i)
            for (j in 0 until newText.length){
                if (!skip.contains(j) && c == newText.get(j)){
                    // 在新字符串上找到旧的字符,记录下
                    skip.add(j)
                    val result = CharacterDiffResult()
                    result.c = c
                    result.fromIndex = i
                    result.moveIndex = j
                    differentList.add(result)
                    break
                }
            }
        }
        return differentList
    }

    /**
     * 查阅oldText上的某个位置上的字符在newText上的位置
     * @param index     oldText上的某一位置
     * @param differentList 记录前后字符位置变化的集合
     * @return  返回对应在newText上的位置，若无返回-1
     */
    fun needMove(index: Int, differentList: List<CharacterDiffResult>): Int {
        for (different in differentList){
            if (different.fromIndex == index) {
                return different.moveIndex
            }
        }
        return -1
    }

    /**
     * 查阅newText上某个位置的字符是不是由oldText变化来的
     * @param index   newText上的某一位置
     * @param differentList 记录位置变化的集合
     * @return  true表示该位置上的字符是由oldText上的字符变化来的
     */
    fun stayHere(index: Int, differentList: List<CharacterDiffResult>): Boolean {
        for (different in differentList){
            if (different.moveIndex == index) return true
        }
        return false
    }

    /**
     * 获取每个字符在某一动画进度时的偏移量
     * @param fromIndex 该字符于oldText时的pos
     * @param moveIndex 该字符于newText时的pos
     * @param progress  当前动画的进度
     * @param oldStartX oldText所处的坐标
     * @param newStartX newText所处的坐标
     * @param oldGaps   oldText上各个字符对应的尺寸
     * @param gaps      newText上各个字符对应的尺寸
     * @return   返回当前进度下，指定字符应该偏移的坐标值
     */
    fun getOffset(fromIndex: Int, moveIndex: Int, progress: Float, oldStartX: Int, newStartX: Int,
                  oldGaps: List<Float>, gaps: List<Float>): Float {
        var dist = newStartX.toFloat()
        for (i in 0 until moveIndex){
            dist += gaps.get(i)
        }
        var cur = oldStartX.toFloat()
        for (i in 0 until fromIndex){
            cur += oldGaps.get(i)
        }
        return cur + (dist - cur) * progress
    }
}