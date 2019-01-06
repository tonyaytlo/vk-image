package com.tony.vkimage.data.entity

import android.support.v4.content.ContextCompat
import com.tony.vkimage.VkApp

class BackgroundRes(private val drawableId: Int, private val thumbnailsId: Int) : BackgroundDrawable() {

    override fun getDrawable() = ContextCompat.getDrawable(VkApp.appContext(), drawableId)

    override fun getThumbnailDrawable() = ContextCompat.getDrawable(VkApp.appContext(), thumbnailsId)
}