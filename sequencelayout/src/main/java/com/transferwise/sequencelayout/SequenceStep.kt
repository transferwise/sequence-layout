package com.transferwise.sequencelayout

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.step_tracker_step.view.*


class SequenceStep(context: Context?, attrs: AttributeSet?)
    : TableRow(context, attrs), ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null)

    private var isActive: Boolean = false

    init {
        View.inflate(getContext(), R.layout.step_tracker_step, this)

        clipToPadding = false
        clipChildren = false

        val attributes = getContext().theme.obtainStyledAttributes(
                attrs,
                R.styleable.SequenceStep,
                0,
                R.style.SequenceStep)

        setupAnchor(attributes)
        setupAnchorTextAppearance(attributes)
        setupTitle(attributes)
        setupTitleTextAppearance(attributes)
        setupSubtitle(attributes)
        setupSubtitleTextAppearance(attributes)
        setupActive(attributes)

        viewTreeObserver.addOnGlobalLayoutListener(this)

        onFinishInflate()
        attributes.recycle()
    }

    /**
     * Sets the anchor label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchor
     */
    fun setAnchor(anchor: CharSequence?) {
        this.anchor.text = anchor
        this.anchor.visibility = View.VISIBLE
        this.anchor.minWidth = resources.getDimensionPixelSize(R.dimen.step_tracker_anchor_min_width)
    }

    /**
     * Sets the anchor text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorTextAppearance
     */
    fun setAnchorTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(anchor, resourceId)
    }

    /**
     * Sets the title label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_title
     */
    fun setTitle(title: CharSequence?) {
        this.title.text = title
        this.title.visibility = View.VISIBLE
    }

    /**
     * Sets the title label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_title
     */
    fun setTitle(@StringRes resId: Int) {
        setTitle(context.getString(resId))
    }

    /**
     * Sets the anchor text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_titleTextAppearance
     */
    fun setTitleTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(title, resourceId)
    }

    /**
     * Sets the subtitle label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitle
     */
    fun setSubtitle(subtitle: CharSequence?) {
        this.subtitle.text = subtitle
        this.subtitle.visibility = View.VISIBLE
    }

    /**
     * Sets the subtitle label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitle
     */
    fun setSubtitle(@StringRes resId: Int) {
        setSubtitle(context.getString(resId))
    }

    /**
     * Sets the subtitle text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitleTextAppearance
     */
    fun setSubtitleTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(subtitle, resourceId)
    }

    /**
     * Returns whether step is active step
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_active
     */
    fun isActive(): Boolean {
        return isActive
    }

    /**
     * Sets whether step is active step
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_active
     */
    fun setActive(isActive: Boolean) {
        this.isActive = isActive
    }

    fun getDot(): SequenceStepDot {
        return dot
    }

    private fun setupAnchor(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_anchor)) {
            anchor.visibility = View.INVISIBLE
        } else {
            setAnchor(attributes.getString(R.styleable.SequenceStep_anchor))
        }
    }

    private fun setupSubtitle(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_subtitle)) {
            subtitle.visibility = View.GONE
        } else {
            setSubtitle(attributes.getString(R.styleable.SequenceStep_subtitle))
        }
    }

    private fun setupTitle(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_title)) {
            title.visibility = View.GONE
        } else {
            setTitle(attributes.getString(R.styleable.SequenceStep_title))
        }
    }

    private fun setupTitleTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_titleTextAppearance)) {
            setTitleTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_titleTextAppearance, 0))
        }
    }

    private fun setupSubtitleTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_subtitleTextAppearance)) {
            setSubtitleTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_subtitleTextAppearance, 0))
        }
    }

    private fun setupAnchorTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_anchorTextAppearance)) {
            setAnchorTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_anchorTextAppearance, 0))
        }
    }

    private fun setupActive(attributes: TypedArray) {
        setActive(attributes.getBoolean(R.styleable.SequenceStep_active, false))
    }

    private fun verticallyCenter(vararg views: View) {
        var maxHeight = 0
        for (view in views) {
            val height = (view as? TextView)?.lineHeight ?: view.measuredHeight
            maxHeight = Math.max(maxHeight, height)
        }
        for (view in views) {
            val height = (view as? TextView)?.lineHeight ?: view.measuredHeight
            view.translationY = (maxHeight - height).toFloat() / 2
        }
    }

    override fun onGlobalLayout() {
        verticallyCenter(anchor, dot, title)
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}