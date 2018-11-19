package com.tony.vkimage.data.entity

import android.graphics.drawable.GradientDrawable

 class BackgroundColor(private val colors: IntArray?) : BackgroundDrawable() {

    override fun getDrawable() = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)

    override fun getThumbnailDrawable() = getDrawable()
}