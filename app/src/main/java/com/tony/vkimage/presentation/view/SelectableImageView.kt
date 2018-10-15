package com.tony.vkimage.presentation.view

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.widget.ImageView
import com.tony.vkimage.R
import ru.galt.app.extensions.dpToPx
import ru.galt.app.extensions.getColorRes

//need optimize
class SelectableImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val pOutBorder = Paint(ANTI_ALIAS_FLAG)
    private val pInBorder = Paint(ANTI_ALIAS_FLAG)
    private var selectedImage: Boolean = false
    private val cornerRadius = 4.dpToPx.toFloat()
    private val rect = RectF()
    private val borderWidth = 2.dpToPx.toFloat()
    private val clipPath = Path()

    init {
        pOutBorder.color = context.getColorRes(R.color.colorPrimary)
        pOutBorder.style = Paint.Style.STROKE
        pOutBorder.strokeWidth = borderWidth + 1.dpToPx

        pInBorder.color = Color.WHITE
        pInBorder.style = Paint.Style.STROKE
        pInBorder.strokeWidth = borderWidth
    }


    override fun onDraw(canvas: Canvas) {
        rect.set(0F,
                0F,
                canvas.width.toFloat(),
                canvas.height.toFloat())
        //simple way
        clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)

        if (selectedImage) {
            rect.set(borderWidth * 2 - 1.dpToPx,
                    borderWidth * 2 - 1.dpToPx,
                    canvas.width.toFloat() - borderWidth * 2 + 1.dpToPx,
                    canvas.height.toFloat() - borderWidth * 2 + 1.dpToPx)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, pInBorder)


            rect.set(borderWidth - 1.dpToPx,
                    borderWidth - 1.dpToPx,
                    canvas.width.toFloat() - borderWidth + 1.dpToPx,
                    canvas.height.toFloat() - borderWidth + 1.dpToPx)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, pOutBorder)
        }
    }


    fun setSelectedImage(selected: Boolean) {
        this.selectedImage = selected
        invalidate()
    }
}