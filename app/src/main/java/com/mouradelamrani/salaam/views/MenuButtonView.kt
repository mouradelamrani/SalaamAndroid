package com.mouradelamrani.salaam.views

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.addListener
import com.mouradelamrani.salaam.R
import com.mouradelamrani.salaam.extensions.dpToPxFloat
import com.mouradelamrani.salaam.extensions.getColor
import kotlin.math.roundToInt

class MenuButtonView : View {

    private val widthPx = dpToPxFloat(25)
    private val heightPx = dpToPxFloat(18)
    private val lineHeight = dpToPxFloat(3)
    private val spaceSize = dpToPxFloat(4.5f)
    private val cornerRadius = dpToPxFloat(1.25f)
    private val midLineMaxWidth = dpToPxFloat(14)
    private val bottomLineMaxWidth = dpToPxFloat(20)
    private val rotatedLineLength = dpToPxFloat(24)
    private val rotatedTranslationX = dpToPxFloat(5)
    private val rotatedTranslationY = -cornerRadius / 2

    private val propertyMidWidth = PropertyValuesHolder.ofFloat("mid_width", midLineMaxWidth, 0f)
    private val propertyMidAlpha = PropertyValuesHolder.ofInt("mid_alpha", 255, 0)
    private val propertyTopRightOpen =
        PropertyValuesHolder.ofFloat("top_right_open", widthPx, rotatedLineLength)
    private val propertyTopRightClose =
        PropertyValuesHolder.ofFloat("top_right_close", rotatedLineLength, widthPx)
    private val propertyBottomRightOpen =
        PropertyValuesHolder.ofFloat("bottom_right_open", bottomLineMaxWidth, rotatedLineLength)
    private val propertyBottomRightClose =
        PropertyValuesHolder.ofFloat("bottom_right_close", rotatedLineLength, bottomLineMaxWidth)
    private val propertyRotation =
        PropertyValuesHolder.ofFloat("lines_rotation", 0f, 45f)
    private val propertyTranslationX =
        PropertyValuesHolder.ofFloat("lines_translation_x", 0f, rotatedTranslationX)
    private val propertyTranslationY =
        PropertyValuesHolder.ofFloat("lines_translation_y", 0f, rotatedTranslationY)

    private var midLineWidth: Float = midLineMaxWidth
    private var topLineRight: Float = widthPx
    private var bottomLineRight: Float = bottomLineMaxWidth
    private var linesRotation = 0f
    private var linesTranslationX = 0f
    private var linesTranslationY = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = getColor(R.color.icon_primary)
    }
    private val paintMidLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = getColor(R.color.icon_primary)
        alpha = 255
    }

    private val rectTopLine: RectF
    private val rectMidLine: RectF
    private val rectBottomLine: RectF

    var isOpen: Boolean = false
        private set(value) {
            field = value
            onToggleListener?.invoke(value)
        }

    private var currentAnimator: ValueAnimator? = null

    private var onToggleListener: ((Boolean) -> Unit)? = null


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle) {
        rectTopLine = RectF(0f, 0f, topLineRight, lineHeight)
        rectMidLine = RectF(
            0f,
            lineHeight + spaceSize,
            midLineWidth,
            lineHeight * 2 + spaceSize
        )
        rectBottomLine = RectF(
            0f,
            2 * lineHeight + 2 * spaceSize,
            bottomLineRight,
            3 * lineHeight + 2 * spaceSize
        )

        isClickable = true
        isFocusable = true
        setOnClickListener {
            if (isOpen) animateClose()
            else animateOpen()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidthMeasureSpec =
            MeasureSpec.makeMeasureSpec(widthPx.roundToInt(), MeasureSpec.EXACTLY)
        val newHeightMeasureSpec =
            MeasureSpec.makeMeasureSpec(heightPx.roundToInt(), MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    fun setOnToggleListener(onToggle: (Boolean) -> Unit) {
        onToggleListener = onToggle
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            // top line
            canvas.save()
            rectTopLine.right = topLineRight
            translate(linesTranslationX, linesTranslationY)
            rotate(linesRotation)
            drawRoundRect(rectTopLine, cornerRadius, cornerRadius, paint)
            canvas.restore()
            // mid line
            rectMidLine.right = midLineWidth
            drawRoundRect(rectMidLine, cornerRadius, cornerRadius, paintMidLine)
            // bottom line
            canvas.save()
            rectBottomLine.right = bottomLineRight
            translate(linesTranslationX, -linesTranslationY)
            rotate(-linesRotation, 0f, heightPx)
            drawRoundRect(rectBottomLine, cornerRadius, cornerRadius, paint)
            canvas.restore()
        }
    }

    fun setState(isOpen: Boolean) {
        if (this.isOpen == isOpen) return
        currentAnimator?.cancel()

        this.isOpen = isOpen
        midLineWidth = if (isOpen) 0f else midLineMaxWidth
        paintMidLine.alpha = if (isOpen) 0 else 255
        topLineRight = if (isOpen) rotatedLineLength else widthPx
        bottomLineRight = if (isOpen) rotatedLineLength else bottomLineMaxWidth
        linesRotation = if (isOpen) 45f else 0f
        linesTranslationX = if (isOpen) rotatedTranslationX else 0f
        linesTranslationY = if (isOpen) rotatedTranslationY else 0f
        invalidate()
    }

    private fun animateOpen() {
        if (isOpen || currentAnimator != null) return
        isOpen = true

        currentAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            setValues(
                propertyMidWidth, propertyMidAlpha, propertyTopRightOpen, propertyBottomRightOpen,
                propertyRotation, propertyTranslationX, propertyTranslationY
            )
            duration = 400
            addUpdateListener { animator ->
                midLineWidth = animator.getAnimatedValue(propertyMidWidth.propertyName) as Float
                paintMidLine.alpha = animator.getAnimatedValue(propertyMidAlpha.propertyName) as Int
                topLineRight = animator.getAnimatedValue(propertyTopRightOpen.propertyName) as Float
                bottomLineRight =
                    animator.getAnimatedValue(propertyBottomRightOpen.propertyName) as Float
                linesRotation =
                    animator.getAnimatedValue(propertyRotation.propertyName) as Float
                linesTranslationX =
                    animator.getAnimatedValue(propertyTranslationX.propertyName) as Float
                linesTranslationY =
                    animator.getAnimatedValue(propertyTranslationY.propertyName) as Float
                invalidate()
            }
            addListener(onEnd = { currentAnimator = null })
            start()
        }
    }

    private fun animateClose() {
        if (!isOpen || currentAnimator != null) return
        isOpen = false

        currentAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            setValues(
                propertyMidWidth, propertyMidAlpha, propertyTopRightClose, propertyBottomRightClose,
                propertyRotation, propertyTranslationX, propertyTranslationY
            )
            duration = 400
            addUpdateListener { animator ->
                midLineWidth =
                    midLineMaxWidth - animator.getAnimatedValue(propertyMidWidth.propertyName) as Float
                paintMidLine.alpha =
                    255 - animator.getAnimatedValue(propertyMidAlpha.propertyName) as Int
                topLineRight =
                    animator.getAnimatedValue(propertyTopRightClose.propertyName) as Float
                bottomLineRight =
                    animator.getAnimatedValue(propertyBottomRightClose.propertyName) as Float
                linesRotation =
                    45 - animator.getAnimatedValue(propertyRotation.propertyName) as Float
                linesTranslationX =
                    rotatedTranslationX - animator.getAnimatedValue(propertyTranslationX.propertyName) as Float
                linesTranslationY =
                    rotatedTranslationY - animator.getAnimatedValue(propertyTranslationY.propertyName) as Float
                invalidate()
            }
            addListener(onEnd = { currentAnimator = null })
            start()
        }
    }

}