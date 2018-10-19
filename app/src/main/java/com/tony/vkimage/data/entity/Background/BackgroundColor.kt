package com.tony.vkimage.data.entity.Background

import android.graphics.drawable.GradientDrawable

class BackgroundColor(private val colors: IntArray?) : Background() {

    override fun getDrawable() = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)

    override fun getThumbnailDrawable() = getDrawable()
}