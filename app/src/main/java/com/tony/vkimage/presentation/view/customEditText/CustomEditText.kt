package com.tony.vkimage.presentation.view.customEditText

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View
import com.tony.vkimage.extension.trimLine
import ru.galt.app.extensions.dpToPx
import kotlin.math.absoluteValue


class CustomEditText : AppCompatEditText {


    companion object {
        const val STYLE_ROUND_RECT_BACKGROUND = 1
        const val STYLE_RECT_BACKGROUND = 2
        const val STYLE_TRANSPARENT_BACKGROUND = 3
    }

    private var selectedStyle = STYLE_ROUND_RECT_BACKGROUND
    private val backgroundPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val linesRect: MutableList<RectView> = mutableListOf()
    private val smoothing = 24.dpToPx.toFloat()
    private val cornerRadius = 6.dpToPx.toFloat()
    private val backgroundPath = Path()


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    init {
        //ET STYLING
        setBackgroundResource(0)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        //
        backgroundPaint.color = Color.WHITE
        backgroundPaint.alpha = 128
    }


    fun setStyle(styleBackground: Int) {
        selectedStyle = styleBackground
        preparePath()
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        onMeasureBackground()
    }

    private fun onMeasureBackground() {
        linesRect.clear()
        for (i in 0 until lineCount) {
            linesRect.add(RectView())
            linesRect[i].height = lineHeight
            if (isEmptyLine(i)) {
                continue // width = 0
            }
            linesRect[i].width = layout.getLineWidth(i).toInt() + paddingLeft + paddingRight
        }
        onRemeasureBackground()
    }

    private fun isEmptyLine(index: Int) = layout.text.substring(layout.getLineStart(index),
            layout.getLineEnd(index)).trimLine.isEmpty()


    private fun onRemeasureBackground() {
        var i = 0
        while (i in 0 until lineCount) {
            if (linesRect[i].width == 0) {//skip empty lines
                i++
                continue
            }
            if (i + 1 < lineCount && linesRect[i + 1].width != 0 && linesRect[i].width != linesRect[i + 1].width &&
                    (linesRect[i].width - linesRect[i + 1].width).absoluteValue < smoothing) { /// если их ширина не сильно отличается
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
                top = if (prevRect == null) {
                    paddingTop + 4.dpToPx
                } else {
                    prevRect.bottom
                }
                right = left + lineRect.width
                bottom = top + lineRect.height
            } else {
                left = 0
                top = if (prevRect == null) {
                    paddingTop
                } else {
                    prevRect.bottom
                }
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
        backgroundPath.rewind() // clear without recreate
        for (i in 0 until linesRect.size) {
            val lineRect = linesRect[i]
            if (lineRect.width == 0) { //skip empty line
                continue
            }
            var topCornerRadius = 0F
            var bottomCornerRadius = 0F

            if (selectedStyle == STYLE_ROUND_RECT_BACKGROUND) {
                topCornerRadius = cornerRadius
                bottomCornerRadius = cornerRadius
            }

            val left = lineRect.left.toFloat()
            val top = lineRect.top.toFloat()
            val right = lineRect.right.toFloat()
            val bottom = lineRect.bottom.toFloat()

            if (i == 0 || linesRect[i - 1].width == 0 || linesRect[i - 1].width <= lineRect.width) {
                if (i != 0 && linesRect[i - 1].width == lineRect.width) {
                    topCornerRadius = 0F
                }
                pathLineBackground(left, top, right, bottom, topCornerRadius, true, path = backgroundPath)

            } else {
                pathLineBackground(left, top, right, bottom, topCornerRadius, false, path = backgroundPath)
            }

            if (i + 1 >= linesRect.size || lineRect.width == 0 || (linesRect[i + 1].width <= linesRect[i].width)) {
                if (i + 1 < linesRect.size && linesRect[i + 1].width == lineRect.width) {
                    bottomCornerRadius = 0F
                }
                pathLineBackground(left, top, right, bottom, -bottomCornerRadius, true, false, backgroundPath, topCornerRadius)
            } else {
                pathLineBackground(left, top, right, bottom, -bottomCornerRadius, false, false, backgroundPath, topCornerRadius)
            }
            backgroundPath.close()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (selectedStyle != STYLE_TRANSPARENT_BACKGROUND) {
            drawBackground(canvas, backgroundPaint)
        }
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas, backgroundPaint: Paint) {
        canvas.drawPath(backgroundPath, backgroundPaint)
    }

    private fun pathLineBackground(left1: Float, top: Float, right1: Float, bottom: Float,
                                   cornerRadius: Float, cuerve: Boolean = true, topSide: Boolean = true, path: Path, topCornerRadius: Float = 0F) {
        val topOrBottom = if (topSide) top else bottom
        val leftOrRight = if (topSide) left1 else right1
        val rightOrLeft = if (topSide) right1 else left1

        if (cuerve) {//positive curve
            if (topSide) {
                path.moveTo(leftOrRight, topOrBottom + cornerRadius)
            } else {
                path.lineTo(leftOrRight, topOrBottom + cornerRadius)
            }
            path.quadTo(leftOrRight, topOrBottom, leftOrRight + cornerRadius, topOrBottom)
            path.lineTo(rightOrLeft - cornerRadius, topOrBottom)
            path.quadTo(rightOrLeft, topOrBottom, rightOrLeft, topOrBottom + cornerRadius)
            if (!topSide) {
                path.lineTo(rightOrLeft, top - topCornerRadius) //
            }
        } else {
            if (topSide) {
                path.moveTo(leftOrRight, topOrBottom + cornerRadius)
            } else {
                path.lineTo(leftOrRight, topOrBottom + cornerRadius)
            }
            path.quadTo(leftOrRight, topOrBottom + cornerRadius / 4, leftOrRight - cornerRadius, topOrBottom)
            path.lineTo(rightOrLeft + cornerRadius, topOrBottom)
            path.quadTo(rightOrLeft, topOrBottom + cornerRadius / 4, rightOrLeft, topOrBottom + cornerRadius)
            if (!topSide) {
                path.lineTo(rightOrLeft, top - topCornerRadius)
            }
        }
    }
}