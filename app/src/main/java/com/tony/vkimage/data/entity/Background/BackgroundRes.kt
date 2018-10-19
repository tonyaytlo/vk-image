package com.tony.vkimage.data.entity.Background

import android.support.v4.content.ContextCompat
import com.tony.vkimage.VkApp

data class BackgroundRes(val drawableId: Int, val thumbnailsId: Int) : Background() {

    override fun getDrawable() = ContextCompat.getDrawable(VkApp.appContext(), drawableId)


    override fun getThumbnailDrawable() = ContextCompat.getDrawable(VkApp.appContext(), thumbnailsId)
}