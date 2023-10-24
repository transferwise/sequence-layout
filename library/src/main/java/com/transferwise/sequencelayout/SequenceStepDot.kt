package com.transferwise.sequencelayout

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.OVAL
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

internal class SequenceStepDot(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var pulseAnimator: AnimatorSet? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.sequence_dot, this, true)
    }

    private val dotView = findViewById<View>(R.id.dotView)
    private val pulseView = findViewById<View>(R.id.pulseView)

    init {
        isEnabled = false
    }

    internal fun setDotBackground(@ColorInt color: Int, @ColorInt progressBackgroundColor: Int) {
        with(StateListDrawable()) {
            setEnterFadeDuration(resources.getInteger(R.integer.sequence_step_duration))
            setExitFadeDuration(resources.getInteger(R.integer.sequence_step_duration))

            addState(intArrayOf(android.R.attr.state_activated),
                with(GradientDrawable()) {
                    shape = OVAL
                    setColor(color)
                    this
                })
            addState(intArrayOf(android.R.attr.state_enabled),
                with(GradientDrawable()) {
                    shape = OVAL
                    setColor(color)
                    setStroke(1.toPx(), Color.TRANSPARENT)
                    this
                })
            addState(intArrayOf(),
                with(GradientDrawable()) {
                    shape = OVAL
                    setColor(progressBackgroundColor)
                    setStroke(1.toPx(), Color.TRANSPARENT)
                    this
                })
            dotView.background = this
        }
    }

    internal fun setPulseColor(@ColorInt color: Int) {
        with(GradientDrawable()) {
            shape = OVAL
            setColor(color)
            pulseView.background = this
        }
    }

    private fun setupAnimator() {
        pulseAnimator =
            (AnimatorInflater.loadAnimator(context, R.animator.fading_pulse) as AnimatorSet).apply {
                setTarget(pulseView)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animator: Animator) {
                        if (isActivated) {
                            animator.start()
                        }
                    }
                })
                start()
            }
    }

    private fun startAnimation() {
        pulseAnimator.let {
            if (it == null) {
                setupAnimator()
            } else if (it.isStarted) {
                return
            }
            pulseView.visibility = VISIBLE
        }
    }

    private fun stopAnimation() {
        pulseAnimator.let {
            if (it == null || !it.isStarted) {
                return
            }
            it.end()
        }
        pulseView.visibility = GONE
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        if (!activated) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        pulseAnimator?.apply {
            removeAllListeners()
            cancel()
        }
        pulseAnimator = null
        super.onDetachedFromWindow()
    }
}