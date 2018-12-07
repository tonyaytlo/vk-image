package com.tony.vkimage.presentation.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tony.vkimage.R

class SquareFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var horizontal = true

    init {
        if (attrs != null) {
            val array = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.SquareFrameLayout,
                    0, 0
            )
            try {
                horizontal = array.getBoolean(R.styleable.SquareFrameLayout_horizontal, true)
            } finally {
                array.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (horizontal) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        }
    }
}