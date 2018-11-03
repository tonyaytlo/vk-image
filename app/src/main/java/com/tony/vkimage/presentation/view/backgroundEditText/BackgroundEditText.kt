package com.tony.vkimage.presentation.view.backgroundEditText

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tony.vkimage.R
import com.tony.vkimage.extension.dpToPx
import com.tony.vkimage.extension.trimLine
import kotlin.math.absoluteValue

class BackgroundEditText : AppCompatEditText {

    companion object {
        private const val TAG = "BackgroundEditText"

        const val STYLE_TRANSPARENT_BACKGROUND = 0
        const val STYLE_ROUND_RECT_BACKGROUND = 1
        const val STYLE_RECT_BACKGROUND = 2
    }

    private val DEFAULT_CORNER_RADIUS = 6.dpToPx.toFloat()
    private val DEFAULT_SMOOTIHNG_WIDTH = 24.dpToPx.toFloat()
    private val DEFAULT_ALPHA = 180
    private val DEFAULT_COLOR = Color.WHITE

    private var selectedStyle = STYLE_ROUND_RECT_BACKGROUND
    private val linesRect: MutableList<RectView> = mutableListOf()

    private var bColor = DEFAULT_COLOR
    private var bAlpha = DEFAULT_ALPHA
    private var bSmoothing = DEFAULT_SMOOTIHNG_WIDTH
    private var bCornerRadius = DEFAULT_CORNER_RADIUS

    private val bPath = Path()
    private val bPaint = Paint()


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    init {
        //config editText
        super.setBackgroundResource(0)
        //tmp
        textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun init(attrs: AttributeSet?) {
        setupAttributes(attrs)
        setupPaint()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.BackgroundEditText,
                0, 0)

        bColor = typedArray.getColor(R.styleable.BackgroundEditText_bColor, DEFAULT_COLOR)
        bAlpha = typedArray.getInt(R.styleable.BackgroundEditText_bAlpha, DEFAULT_ALPHA)
        bSmoothing = typedArray.getDimension(R.styleable.BackgroundEditText_bSmoothing, DEFAULT_SMOOTIHNG_WIDTH)
        bCornerRadius = typedArray.getDimension(R.styleable.BackgroundEditText_bCornerRadius, DEFAULT_CORNER_RADIUS)

        typedArray.recycle()
    }

    private fun setupPaint() {
        bPaint.isAntiAlias = true
        bPaint.color = bColor
        bPaint.alpha = bAlpha
        bPaint.pathEffect = CornerPathEffect(bCornerRadius)
    }

    override fun setBackground(background: Drawable?) {
        Log.w(TAG, "view does not support background drawable")
    }

    override fun setBackgroundResource(resId: Int) {
        Log.w(TAG, "view does not support background resource")
    }

    fun setBackgroundAlpha(alpha: Int) {
        if (alpha == bAlpha) {
            return
        }
        bAlpha = alpha
        bPaint.alpha = bAlpha
        invalidate()
    }

    fun getBackgroundAlpha() = bAlpha

    override fun setBackgroundColor(color: Int) {
        if (color == bColor) {
            return
        }
        bColor = color
        bPaint.color = bColor
        invalidate()
    }

    fun setBackgroundCorner(radius: Float) {
        if (radius == bCornerRadius || radius < 0F) {
            return
        }
        bCornerRadius = radius
        bPaint.pathEffect = CornerPathEffect(radius)
        preparePath()
        invalidate()
    }

    fun getBackgroundCorner() = bCornerRadius

    fun setSmoothing(width: Float) {
        if (width == bSmoothing || width < 0F) {
            return
        }
        bSmoothing = width
        preparePath()
        invalidate()
    }

    fun getSmoothing() = bSmoothing

    //delete
    fun setBackgroundStyle(styleBackground: Int) {
        if (styleBackground == selectedStyle) {
            return
        }

        when (styleBackground) {
            STYLE_RECT_BACKGROUND -> {
                bCornerRadius = 0F
                selectedStyle = styleBackground
                preparePath()
            }
            STYLE_ROUND_RECT_BACKGROUND -> {
                bCornerRadius = DEFAULT_CORNER_RADIUS
                selectedStyle = styleBackground
                preparePath()
            }
            STYLE_TRANSPARENT_BACKGROUND -> {
                selectedStyle = styleBackground
            }
            else -> return
        }
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        onMeasureBackground()
    }

    private fun onMeasureBackground() {
        linesRect.clear()
        for (i in 0 until lineCount) {
            linesRect.add(RectView(height = lineHeight))
            if (isEmptyLine(i)) {
                continue
            }
            linesRect[i].width = layout.getLineWidth(i).toInt() + paddingLeft + paddingRight
        }
        onRemeasureBackground()
    }

    private fun onRemeasureBackground() {
        var i = 0
        while (i in 0 until lineCount) {
            if (linesRect[i].width == 0) {//skip empty lines
                i++
                continue
            }
            if (i + 1 == lineCount || linesRect[i + 1].width == 0) {
                i++ //if it last line or last line is empty
                continue
            }

            if (linesRect[i].width != linesRect[i + 1].width &&
                    (linesRect[i].width - linesRect[i + 1].width).absoluteValue < bSmoothing) { /// если их ширина не сильно отличается

                if (linesRect[i + 1].width > linesRect[i].width) {//приводим их к большей ширине
                    linesRect[i].width = linesRect[i + 1].width
                    if (i > 0) {
                        i-- // go back to remeasure
                        continue
                    }
                } else {
                    linesRect[i + 1].width = linesRect[i].width
                }
            }
            i++
        }

        onLayoutBackground()
    }

    private fun onLayoutBackground() {
        var prevRect: RectView? = null
        for (i in 0 until linesRect.size) {
            val lineRect = linesRect[i]

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            if (textAlignment == View.TEXT_ALIGNMENT_CENTER) {
                val centerX = (width / 2)
                val centerRect = (lineRect.width / 2)

                left = centerX - centerRect
                top = prevRect?.bottom ?: paddingTop + 4.dpToPx
                right = left + lineRect.width
                bottom = top + lineRect.height
            } else {
                left = 0
                top = prevRect?.bottom ?: paddingTop
                right = lineRect.width
                bottom = top + lineRect.height
            }

            lineRect.left = left
            lineRect.top = top
            lineRect.right = right
            lineRect.bottom = bottom

            prevRect = lineRect
        }
        preparePath()
    }


    private fun preparePath() {
        bPath.reset()
        val part = mutableListOf<RectView>()
        linesRect.forEach {
            if (it.width == 0) {
                if (part.isNotEmpty()) {
                    preparePartPath(part)
                    part.clear()
                }
            } else {
                part.add(it)
            }
        }
        if (part.isNotEmpty()) {
            preparePartPath(part)
        }
    }

    private fun preparePartPath(lineBounds: MutableList<RectView>) {
        bPath.moveTo(lineBounds[0].left.toFloat(), lineBounds[0].top.toFloat())
        lineBounds.forEach {
            bPath.lineTo(it.right.toFloat(), it.top.toFloat())
            bPath.lineTo(it.right.toFloat(), it.bottom.toFloat())
        }
        lineBounds.asReversed().forEach {
            bPath.lineTo(it.left.toFloat(), it.bottom.toFloat())
            bPath.lineTo(it.left.toFloat(), it.top.toFloat())
        }
        bPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas, bPaint)
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas, paint: Paint) {
        if (selectedStyle != STYLE_TRANSPARENT_BACKGROUND) {
            canvas.drawPath(bPath, paint)
        }
    }

    private fun isEmptyLine(index: Int) = layout.text.substring(layout.getLineStart(index),
            layout.getLineEnd(index)).trimLine.isEmpty()


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.style = selectedStyle
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        val ss = state
        super.onRestoreInstanceState(ss.superState)
        selectedStyle = ss.style
    }

    private class SavedState(p: Parcelable) : View.BaseSavedState(p) {
        internal var style: Int = STYLE_ROUND_RECT_BACKGROUND
    }
}