package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.tony.vkimage.R
import com.tony.vkimage.extension.dpToPx
import com.tony.vkimage.extension.getColorRes

//may optimize
// fix round -> background drawable, background
class SelectableImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val pOutBorder = Paint()
    private val rOutBorder = RectF()

    private val pInBorder = Paint()
    private val rInBorder = RectF()

    private val borderWidth = 2.dpToPx.toFloat()
    private val cornerRadius = 4.dpToPx.toFloat()

    private val pBackground = Paint()
    private val rect = RectF()
    private val clipPath = Path()

    private var backgroundColor: Int? = null
    private var selectedImage: Boolean = false


    init {
        pOutBorder.color = context.getColorRes(R.color.colorBtn)
        pOutBorder.style = Paint.Style.STROKE
        pOutBorder.strokeWidth = borderWidth + 1

        pInBorder.color = Color.WHITE
        pInBorder.style = Paint.Style.STROKE
        pInBorder.strokeWidth = borderWidth + 1.dpToPx

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        prepareRect(w, h)
    }

    private fun prepareRect(w: Int, h: Int) {
        rInBorder.set(borderWidth + borderWidth / 2,
                borderWidth + borderWidth / 2,
                h.toFloat() - borderWidth - borderWidth / 2,
                w.toFloat() - borderWidth - borderWidth / 2)
        rOutBorder.set(borderWidth / 2,
                borderWidth / 2,
                w.toFloat() - borderWidth / 2,
                h.toFloat() - borderWidth / 2)
        rect.set(0F,
                0F,
                w.toFloat(),
                h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        //simple way
        clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        if (backgroundColor != null) {
            canvas.drawRect(rect, pBackground)
        }

        super.onDraw(canvas) // draw bitmap

        if (selectedImage) {
            canvas.drawRect(rInBorder, pInBorder)
            canvas.drawRoundRect(rOutBorder, cornerRadius, cornerRadius, pOutBorder)
        }
    }


    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        pBackground.color = color
        invalidate()
    }

    fun setSelectedImage(selected: Boolean) {
        this.selectedImage = selected
        invalidate()
    }
}