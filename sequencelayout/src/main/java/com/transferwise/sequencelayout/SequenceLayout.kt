package com.transferwise.sequencelayout

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.sequence_layout.view.*
import kotlinx.android.synthetic.main.sequence_step.view.*

/**
 * Vertical step tracker that contains {@link com.transferwise.sequencelayout.SequenceStep}s and animates to the first active step.
 *
 * <pre>
 * &lt;com.transferwise.sequencelayout.SequenceLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      app:progressForegroundColor="?colorAccent"
 *      app:progressBackgroundColor="#ddd"&gt;
 *
 *      &lt;com.transferwise.sequencelayout.SequenceStep ... /&gt;
 *      &lt;com.transferwise.sequencelayout.SequenceStep app:active="true" ... /&gt;
 *      &lt;com.transferwise.sequencelayout.SequenceStep ... /&gt;
 *
 * &lt;/com.transferwise.sequencelayout.SequenceLayout&gt;
 * </pre>
 *
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceLayout_progressForegroundColor
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceLayout_progressBackgroundColor
 *
 * @see com.transferwise.sequencelayout.SequenceStep
 */
public class SequenceLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr), ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(getContext(), R.layout.sequence_layout, this)

        val attributes = getContext().theme.obtainStyledAttributes(
                attrs,
                R.styleable.SequenceLayout,
                0,
                R.style.SequenceLayout)
        applyAttributes(attributes)
        attributes.recycle()

        clipToPadding = false
        clipChildren = false

        onFinishInflate()
        start()
    }

    @ColorInt
    private var progressBackgroundColor: Int = 0

    @ColorInt
    private var progressForegroundColor: Int = 0

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is SequenceStep) {
            if (child.isActive()) {
                child.setPadding(
                        0,
                        if (stepsWrapper.childCount == 0) 0 else resources.getDimensionPixelSize(R.dimen.sequence_active_step_padding_top), //no paddingTop if first step is active
                        0,
                        resources.getDimensionPixelSize(R.dimen.sequence_active_step_padding_bottom)
                )
            }
            val dot = child.getDot()
            dot.setDotBackground(progressForegroundColor, progressBackgroundColor)
            dot.setPulseColor(progressForegroundColor)
            stepsWrapper.addView(child, params)
            return
        }
        super.addView(child, index, params)
    }

    override fun onGlobalLayout() {
        if (stepsWrapper.childCount > 0) {
            adaptProgressBarHeight()
            setProgressBarHorizontalOffset()
            animateToActive()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    fun setStyle(@StyleRes defStyleAttr: Int) {
        val attributes = context.theme.obtainStyledAttributes(defStyleAttr, R.styleable.SequenceLayout)
        applyAttributes(attributes)
        attributes.recycle()
    }

    /**
     * Sets the progress bar color
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceLayout_progressForegroundColor
     */
    fun setProgressForegroundColor(@ColorInt color: Int) {
        this.progressForegroundColor = color
        progressBarForeground.setBackgroundColor(color)
        //TODO apply to existing steps
    }

    /**
     * Sets background resource for the dot of each contained step
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceLayout_dotBackground
     */
    fun setProgressBackgroundColor(@ColorInt progressBackgroundColor: Int) {
        this.progressBackgroundColor = progressBackgroundColor
        progressBarBackground.setBackgroundColor(progressBackgroundColor)
        //TODO apply to existing steps
    }

    /**
     * Removes all contained [com.transferwise.sequencelayout.SequenceStep]s
     */
    fun removeAllSteps() {
        stepsWrapper.removeAllViews()
    }

    /**
     * Replaces all contained [com.transferwise.sequencelayout.SequenceStep]s with those provided and bound by the adapter
     */
    fun setAdapter(adapter: SequenceAdapter<SequenceStep>) {
        removeAllSteps()
        val count = adapter.getCount()
        for (i in 0 until count) {
            val item = adapter.getItem(i)
            val view = SequenceStep(context)
            adapter.bindView(view, item)
            addView(view)
        }
        start()
    }

    private fun applyAttributes(attributes: TypedArray) {
        setupProgressForegroundColor(attributes)
        setupDotBackground(attributes)
    }

    private fun setupProgressForegroundColor(attributes: TypedArray) {
        setProgressForegroundColor(attributes.getColor(R.styleable.SequenceLayout_progressForegroundColor, 0))
    }

    private fun setupDotBackground(attributes: TypedArray) {
        setProgressBackgroundColor(attributes.getColor(R.styleable.SequenceLayout_progressBackgroundColor, 0))
    }

    private fun setProgressBarHorizontalOffset() {
        val firstAnchor: View = stepsWrapper.getChildAt(0).anchor.findViewById(R.id.anchor)
        val firstDot: View = stepsWrapper.getChildAt(0).findViewById(R.id.dot)
        progressBarWrapper.translationX = firstAnchor.measuredWidth + (firstDot.measuredWidth - progressBarWrapper.measuredWidth) / 2f
    }

    private fun animateToActive() {
        val stepsToAnimate = getStepOffsets()
        if (stepsToAnimate.size() > 0) {
            val lastChild = stepsWrapper.getChildAt(stepsWrapper.childCount - 1)
            val lastChildOffset = lastChild.top + lastChild.paddingTop
            progressBarForeground.visibility = VISIBLE
            progressBarForeground.pivotY = 0f
            progressBarForeground.scaleY = 0f
            progressBarForeground
                    .animate()
                    .setStartDelay(resources.getInteger(R.integer.sequence_step_duration).toLong())
                    .scaleY(stepsToAnimate.keyAt(stepsToAnimate.size() - 1) / lastChildOffset.toFloat())
                    .setInterpolator(LinearInterpolator())
                    .setDuration((stepsToAnimate.size() - 1) * resources.getInteger(R.integer.sequence_step_duration).toLong())
                    .setUpdateListener({
                        val v = (progressBarForeground.scaleY * lastChildOffset)

                        for (i in 0..stepsToAnimate.size()) {
                            if (stepsToAnimate.valueAt(i) != null) {
                                val step = stepsToAnimate.valueAt(i) as SequenceStep
                                if (stepsToAnimate.keyAt(i) <= v + step.paddingTop) {
                                    if (i == stepsToAnimate.size() - 1) {
                                        step.getDot().isActivated = true
                                    } else {
                                        step.getDot().isEnabled = true
                                    }
                                    stepsToAnimate.setValueAt(i, null)
                                }
                            }
                        }
                    })
                    .start()
        }
    }

    private fun adaptProgressBarHeight() {
        val layoutParams = progressBarWrapper.layoutParams
        val lastChild = stepsWrapper.getChildAt(stepsWrapper.childCount - 1)
        layoutParams.height = lastChild.top + lastChild.paddingTop
        progressBarWrapper.layoutParams = layoutParams
    }

    private fun getStepOffsets(): SparseArray<View> {
        val childCount = stepsWrapper.childCount
        val stepOffsets = SparseArray<View>()
        var containsActiveStep = false
        for (i in 0 until childCount) {
            val step = stepsWrapper.getChildAt(i) as SequenceStep
            stepOffsets.append(step.top - progressBarForeground.top + step.getDot().top, step)
            if (step.isActive()) {
                containsActiveStep = true
                if (i == childCount - 1) {
                    //remove bottom padding if active step is last step
                    step.setPadding(step.paddingLeft, step.paddingTop, step.paddingRight, 0)
                }
                break
            }
        }
        if (!containsActiveStep) {
            stepOffsets.clear()
        }
        return stepOffsets
    }

    fun start() {
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }
}