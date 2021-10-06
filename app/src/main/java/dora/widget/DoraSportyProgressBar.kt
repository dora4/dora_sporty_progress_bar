package dora.widget

import android.animation.Animator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class DoraSportyProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var progressType = PROGRESS_TYPE_FLAT
    private var progressBgRect = RectF()
    private var progressBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressHoverPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressWidth = 0f
    private var progressBgColor = 0
    private var progressHoverColor = 0
    private var percentRate = 0f
    private var angle = 0
    private var animationTime = 0

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DoraSportyProgressBar, defStyleAttr, 0)
        when (a.getInt(R.styleable.DoraSportyProgressBar_dora_progressType, PROGRESS_TYPE_FLAT)) {
            0 -> progressType = PROGRESS_TYPE_FLAT
            1 -> progressType = PROGRESS_TYPE_SEMICIRCLE_TOP
            2 -> progressType = PROGRESS_TYPE_SEMICIRCLE_BOTTOM
        }
        progressWidth = a.getDimension(R.styleable.DoraSportyProgressBar_dora_progressWidth, 30f)
        progressBgColor = a.getColor(R.styleable.DoraSportyProgressBar_dora_progressBgColor, Color.GRAY)
        progressHoverColor = a.getColor(R.styleable.DoraSportyProgressBar_dora_progressHoverColor, Color.BLUE)
        animationTime = a.getInt(R.styleable.DoraSportyProgressBar_dora_animationTime, 1000)
        a.recycle()
    }

    private fun initPaints() {
        progressBgPaint.style = Paint.Style.STROKE
        progressBgPaint.strokeCap = Paint.Cap.ROUND
        progressBgPaint.color = progressBgColor
        progressBgPaint.strokeWidth = progressWidth
        progressHoverPaint.style = Paint.Style.STROKE
        progressHoverPaint.strokeCap = Paint.Cap.ROUND
        progressHoverPaint.color = progressHoverColor
        progressHoverPaint.strokeWidth = progressWidth
    }

    /**
     * 两次调用间隔需要大于animationTime。
     *
     * @param rate 0~1的小数
     */
    fun setPercentRate(rate: Float) {
        val animator = ValueAnimator.ofObject(
                AnimationEvaluator(),
                percentRate,
                rate)
        animator.addUpdateListener { animation: ValueAnimator ->
            val value = animation.animatedValue as Float
            angle = (value * 180).toInt()
            percentRate = value
            invalidate()
        }
        animator.interpolator = DecelerateInterpolator()
        animator.setDuration(animationTime.toLong()).start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                percentRate = rate
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private inner class AnimationEvaluator : TypeEvaluator<Float> {
        override fun evaluate(fraction: Float, startValue: Float, endValue: Float): Float {
            return if (endValue > startValue) {
                startValue + fraction * (endValue - startValue)
            } else {
                startValue - fraction * (startValue - endValue)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        var measuredHeight = measuredHeight
        if (progressType != PROGRESS_TYPE_FLAT) {
            val min = measuredWidth.coerceAtMost(measuredHeight)
            var left = 0f
            val top = 0f
            var right = 0f
            var bottom = 0f
            if (measuredWidth >= min) {
                left = ((measuredWidth - min) / 2).toFloat()
                right = left + min
            }
            if (measuredHeight >= min) {
                bottom = top + min
            }
            if (progressWidth > min / 3) {
                progressWidth = (min / 3).toFloat()
            }
            progressBgPaint.strokeWidth = progressWidth
            progressHoverPaint.strokeWidth = progressWidth
            progressBgRect[left + progressWidth / 2, top + progressWidth / 2, right - progressWidth / 2] = bottom - progressWidth / 2
            setMeasuredDimension(MeasureSpec.makeMeasureSpec((right - left).toInt(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((bottom - top + progressWidth).toInt() / 2, MeasureSpec.EXACTLY))
        } else {
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                progressWidth = 30f
            } else {
                if (measuredHeight > measuredWidth / 2) {
                    measuredHeight = measuredWidth / 2
                }
                progressWidth = measuredHeight.toFloat()
            }
            progressBgPaint.strokeWidth = progressWidth
            progressHoverPaint.strokeWidth = progressWidth
            progressBgRect[0f, 0f, measuredWidth.toFloat()] = measuredHeight.toFloat()
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(progressWidth.toInt(), MeasureSpec.EXACTLY))
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (progressType == PROGRESS_TYPE_FLAT) {
            canvas.drawLine(progressBgRect.left + progressWidth / 2, progressWidth / 2,
                    progressBgRect.right - progressWidth / 2, progressWidth / 2, progressBgPaint)
            if (percentRate > 0) {
                canvas.drawLine(progressBgRect.left + progressWidth / 2, progressWidth / 2,
                        (progressBgRect.right - progressWidth / 2) * percentRate, progressWidth / 2, progressHoverPaint)
            }
        } else if (progressType == PROGRESS_TYPE_SEMICIRCLE_TOP) {
            canvas.drawArc(progressBgRect, 180f, 180f, false, progressBgPaint)
            canvas.drawArc(progressBgRect, 180f, angle.toFloat(), false, progressHoverPaint)
        } else if (progressType == PROGRESS_TYPE_SEMICIRCLE_BOTTOM) {
            canvas.translate(0f, -progressBgRect.height() / 2)
            canvas.drawArc(progressBgRect, 0f, 180f, false, progressBgPaint)
            canvas.drawArc(progressBgRect, 180f, -angle.toFloat(), false, progressHoverPaint)
        }
    }

    companion object {
        const val PROGRESS_TYPE_FLAT = 0
        const val PROGRESS_TYPE_SEMICIRCLE_TOP = 1
        const val PROGRESS_TYPE_SEMICIRCLE_BOTTOM = 2
    }

    init {
        initAttrs(context, attrs, defStyleAttr)
        initPaints()
    }
}