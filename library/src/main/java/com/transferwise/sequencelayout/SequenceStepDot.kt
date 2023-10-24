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

internal class SequenceStepDot @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
        pulseAnimator = AnimatorInflater.loadAnimator(context, R.animator.fading_pulse) as AnimatorSet
        pulseAnimator!!.setTarget(pulseView)
        pulseAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                if (isActivated) {
                    animator.start()
                }
            }
        })
    }

    private fun startAnimation() {
        if (pulseAnimator == null) {
            setupAnimator()
        }
        if (pulseAnimator!!.isStarted) {
            return
        }

        pulseView.visibility = VISIBLE
        pulseAnimator!!.start()
    }

    private fun stopAnimation() {
        if (pulseAnimator == null || !pulseAnimator!!.isStarted) {
            return
        }
        pulseAnimator!!.end()
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
        pulseAnimator?.cancel()
        super.onDetachedFromWindow()
    }
}