package com.tony.vkimage.data.repository

import android.content.Context
import com.tony.vkimage.data.entity.BackgroundDrawable
import com.tony.vkimage.data.repository.datasource.BackgroundManager

class BackgroundRepository constructor(private val backgroundManager: BackgroundManager,
                                       private val context: Context) {

    private var backgrounds: MutableList<BackgroundDrawable>? = null

    fun getBackgrounds() = if (backgrounds != null) backgrounds!!
    else backgroundManager.getBackgrounds(context)

}