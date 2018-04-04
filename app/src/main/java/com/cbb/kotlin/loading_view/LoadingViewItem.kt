package com.cbb.kotlin.loading_view

import android.animation.ValueAnimator
import android.graphics.*
import android.view.animation.DecelerateInterpolator

/**
 * Created by CBB on 2018/3/28.
 * describe:
 */
class LoadingViewItem(var loadingView: LoadingView, var centerPoint: PointF, length: Int) {

    companion object {
        /* 画笔圆滑比例，数值越小，越圆滑 */
        val CORNER_PATH_EFFECT_SCALE = 8
        /* 动画持续时间 */
        val ANIMATION_DURATION = 200L
    }

    var mScale: Float = 0f
    var paint: Paint
    lateinit var corEffect: CornerPathEffect
    lateinit var path: Path
    lateinit var mShowAnimation: ValueAnimator
    lateinit var mHideAnimation: ValueAnimator


    init {
        paint = Paint()
        paint.color = loadingView.color
        paint.alpha = 0
        paint.strokeWidth = 3f
        corEffect = CornerPathEffect((length / CORNER_PATH_EFFECT_SCALE).toFloat())
        paint.setPathEffect(corEffect)  // 路径效果为圆角型

        var points = getHexagonPoints(centerPoint, length)
        path = Path()
        path.moveTo(points[1]!!.x, points[1]!!.y)
        path.lineTo(points[2]!!.x, points[2]!!.y)
        path.lineTo(points[3]!!.x, points[3]!!.y)
        path.lineTo(points[4]!!.x, points[4]!!.y)
        path.lineTo(points[5]!!.x, points[5]!!.y)
        path.lineTo(points[6]!!.x, points[6]!!.y)
        path.close()

        initAnimation()
    }

    private fun initAnimation() {
        mShowAnimation = ValueAnimator.ofFloat(0f, 1f)
        mShowAnimation.duration = ANIMATION_DURATION
        mShowAnimation.interpolator = DecelerateInterpolator()
        mShowAnimation.addUpdateListener { animation ->
            // 根据动画属性变化，刷新view
            val animValue = animation.getAnimatedValue() as Float
            mScale = 0.5f + animValue / 2
            paint.alpha = (animValue * 255).toInt()
            loadingView.invalidate()
        }

        mHideAnimation = ValueAnimator.ofFloat(1f, 0f)
        mHideAnimation.duration = ANIMATION_DURATION
        mHideAnimation.interpolator = DecelerateInterpolator()
        mHideAnimation.addUpdateListener { animation ->
            val animValue = animation.getAnimatedValue() as Float
            mScale = 0.5f + animValue / 2
            paint.alpha = (animValue * 255).toInt()
            loadingView.invalidate()
        }
    }

    private fun getHexagonPoints(centerPoint: PointF, height: Int): Array<PointF?> {
        var points = arrayOfNulls<PointF>(7)
        var length = (height / Math.tan(Math.PI / 3) * 2).toFloat()
        points[0] = centerPoint
        points[1] = PointF(centerPoint.x, centerPoint.y - length)
        points[2] = PointF(centerPoint.x + height, centerPoint.y - length / 2)
        points[3] = PointF(centerPoint.x + height, centerPoint.y + length / 2)
        points[4] = PointF(centerPoint.x, centerPoint.y + length)
        points[5] = PointF(centerPoint.x - height, centerPoint.y + length / 2)
        points[6] = PointF(centerPoint.x - height, centerPoint.y - length / 2)

        return points
    }

    fun drawViewItem(canvas: Canvas) {
        canvas.save()
        canvas.scale(mScale, mScale, centerPoint.x, centerPoint.y)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    fun reset() {
        mScale = 0f
        paint.alpha = 0
        loadingView.invalidate()
    }

    fun setColor(color: Int) {
        paint.color = color
        loadingView.invalidate()
    }
}