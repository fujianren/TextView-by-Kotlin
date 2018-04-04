package com.cbb.kotlin.loading_view

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.cbb.kotlin.R

/**
 * Created by CBB on 2018/3/28.
 * describe:
 */

class LoadingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        /* 偏移量的缩放比例，数值越大，则每个小六边形越大。根据极值定义，数值不能小于1 */
        val OFFSET_SCALE = 20
    }

    var color: Int = Color.GRAY
    fun setPaintColor(value: Int) {
        for (item in items) {
            item.setColor(value)
        }
        invalidate()
    }

    init {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.loadingView, 0, 0)
        color = attributes.getColor(R.styleable.loadingView_view_color, Color.GRAY)
        attributes.recycle()
    }

    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private var mViewLength: Int = 0
    private var items: ArrayList<LoadingViewItem> = arrayListOf();
    private var mPlayAnimator = AnimatorSet()
    private var isStart = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2;
        mCenterY = h / 2;
        mViewLength = (if (w > h) h else w) / 3
        initViewItems()
        initAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isStart) {
            for (item in items) {
                item.drawViewItem(canvas)
            }
        }
    }

    private fun initAnimation() {
        var animators = arrayListOf<Animator>()
        // 依次添加显示动画，再添加消失动画
        for (item in items) {
            animators.add(item.mShowAnimation)
        }
        for (item in items) {
            animators.add(item.mHideAnimation)
        }

        mPlayAnimator.playSequentially(animators)   // 顺序执行
    }

    private fun initViewItems() {
        var points = getHexagonPoints(PointF(mCenterX.toFloat(), mCenterY.toFloat()), mViewLength)
        var itemLength = mViewLength / 2
        var offsetLenght = itemLength / OFFSET_SCALE
        for (point in points) {
            items.add(LoadingViewItem(this, point!!, itemLength - offsetLenght))
        }
    }

    /*******************************************
     * 暴露的公共方法
     *******************************************/
    fun start() {
        if (mPlayAnimator == null || mPlayAnimator.isRunning || mPlayAnimator.isStarted) return

        mPlayAnimator.removeAllListeners()
        mPlayAnimator.addListener(OWRepeatListener())
        mPlayAnimator.start()
        isStart = true
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pause() {
        mPlayAnimator?.pause()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun resume() {
        mPlayAnimator?.resume()
    }

    fun end() {
//        if (mPlayAnimator == null) return
        resetAllItem()
        mPlayAnimator?.removeAllListeners()
        mPlayAnimator?.end()
        isStart = false
    }


    private fun resetAllItem() {
        for (item in items) {
            item.reset()
        }
        invalidate()
    }

    /**
     * 获取7个六边形的中心点坐标
     * @param centerPoint   本view的中心点坐标
     * @param length        每个六边形中心之间的距离
     * @return
     */
    fun getHexagonPoints(centerPoint: PointF, length: Int): Array<PointF?> {
        var points = arrayOfNulls<PointF>(7)

        var height = (Math.sin(Math.PI / 3) * length).toFloat()
        points[0] = PointF(centerPoint.x - length / 2, centerPoint.y - height)  // 左上
        points[1] = PointF(centerPoint.x + length / 2, centerPoint.y - height)  // 右上
        points[2] = PointF(centerPoint.x + length, centerPoint.y)                 // 正右
        points[3] = PointF(centerPoint.x + length / 2, centerPoint.y + height)  // 右下
        points[4] = PointF(centerPoint.x - length / 2, centerPoint.y + height)  // 左下
        points[5] = PointF(centerPoint.x - length, centerPoint.y)                  // 正左
        points[6] = centerPoint  // 中心点

        return points
    }


    class OWRepeatListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            animation?.start()
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationStart(animation: Animator?) {}
    }
}
