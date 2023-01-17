package com.transferwise.sequencelayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.doOnPreDraw
import androidx.core.widget.TextViewCompat
import kotlin.math.max

/**
 * Step, represented in a row inside of {@link com.transferwise.sequencelayout.SequenceLayout}.
 *
 * <pre>
 * &lt;com.transferwise.sequencelayout.SequenceStep
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      app:active="true"
 *      app:anchor="Anchor"
 *      app:anchorMinWidth="20dp"
 *      app:anchorMaxWidth="80dp"
 *      app:anchorTextAppearance="@style/TextAppearance.AppCompat.Small"
 *      app:subtitle="This is a subtitle"
 *      app:subtitleTextAppearance="@style/TextAppearance.AppCompat.Body1"
 *      app:title="Third step"
 *      app:titleTextAppearance="@style/TextAppearance.AppCompat.Title" /&gt;
 * </pre>
 *
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchor
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorTextAppearance
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorMinWidth
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorMaxWidth
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorTextAppearance
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_title
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_titleTextAppearance
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitle
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitleTextAppearance
 * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_active
 *
 * @see com.transferwise.sequencelayout.SequenceLayoutView
 */
class SequenceStepView(context: Context?, attrs: AttributeSet?) : TableRow(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var isActive: Boolean = false
    internal var onStepChangedListener: OnStepChangedListener? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.sequence_step, this, true)
    }

    private val anchor = findViewById<TextView>(R.id.anchor)
    private val title = findViewById<TextView>(R.id.title)
    private val subtitle = findViewById<TextView>(R.id.subtitle)

    init {
        clipToPadding = false
        clipChildren = false

        val attributes = getContext().theme.obtainStyledAttributes(
            attrs,
            R.styleable.SequenceStep,
            0,
            R.style.SequenceStep
        )

        setupAnchor(attributes)
        setupAnchorWidth(attributes)
        setupAnchorTextAppearance(attributes)
        setupTitle(attributes)
        setupTitleTextAppearance(attributes)
        setupSubtitle(attributes)
        setupSubtitleTextAppearance(attributes)
        setupActive(attributes)

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
        this.anchor.minWidth = resources.getDimensionPixelSize(R.dimen.sequence_anchor_min_width)
    }

    /**
     * Sets the anchor max width
     */
    fun setAnchorMaxWidth(@Dimension(unit = Dimension.PX) maxWidth: Int) {
        anchor.maxWidth = maxWidth
    }

    /**
     * Sets the anchor min width
     */
    fun setAnchorMinWidth(@Dimension(unit = Dimension.PX) minWidth: Int) {
        anchor.minWidth = minWidth
    }

    /**
     * Sets the anchor text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorTextAppearance
     */
    fun setAnchorTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(anchor, resourceId)
        verticallyCenter(anchor, title)
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
        verticallyCenter(anchor, title)
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
        doOnPreDraw { onStepChangedListener?.onStepChanged() }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        doOnPreDraw { onStepChangedListener?.onStepChanged() }
    }

    fun getDotOffset(): Int =
        (max(
            getViewHeight(anchor),
            getViewHeight(title)
        ) - 8.toPx()) / 2 //TODO dynamic dot height

    private fun setupAnchor(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_anchor)) {
            anchor.visibility = View.INVISIBLE
        } else {
            setAnchor(attributes.getString(R.styleable.SequenceStep_anchor))
        }
    }

    private fun setupAnchorWidth(attributes: TypedArray) {
        setAnchorMinWidth(
            attributes.getDimensionPixelSize(
                R.styleable.SequenceStep_anchorMinWidth,
                0
            )
        )
        setAnchorMaxWidth(
            attributes.getDimensionPixelSize(
                R.styleable.SequenceStep_anchorMaxWidth,
                Integer.MAX_VALUE
            )
        )
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
            setTitleTextAppearance(
                attributes.getResourceId(
                    R.styleable.SequenceStep_titleTextAppearance,
                    0
                )
            )
        }
    }

    private fun setupSubtitleTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_subtitleTextAppearance)) {
            setSubtitleTextAppearance(
                attributes.getResourceId(
                    R.styleable.SequenceStep_subtitleTextAppearance,
                    0
                )
            )
        }
    }

    private fun setupAnchorTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_anchorTextAppearance)) {
            setAnchorTextAppearance(
                attributes.getResourceId(
                    R.styleable.SequenceStep_anchorTextAppearance,
                    0
                )
            )
        }
    }

    private fun setupActive(attributes: TypedArray) {
        setActive(attributes.getBoolean(R.styleable.SequenceStep_active, false))
    }

    private fun verticallyCenter(vararg views: View) {
        val maxHeight = views.map(::getViewHeight).maxOrNull() ?: 0

        views.forEach { view ->
            val height = getViewHeight(view)
            (view.layoutParams as MarginLayoutParams).topMargin = (maxHeight - height) / 2
            view.requestLayout()
        }
    }

    private fun getViewHeight(view: View) =
        if (view is TextView) {
            ((view.lineHeight - view.lineSpacingExtra) / view.lineSpacingMultiplier).toInt()
        } else {
            view.measuredHeight
        }

    internal interface OnStepChangedListener {
        fun onStepChanged()
    }
}