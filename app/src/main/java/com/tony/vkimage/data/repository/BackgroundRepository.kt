package com.tony.vkimage.data.repository

import android.content.Context
import com.tony.vkimage.data.repository.datasource.BackgroundManager

class BackgroundRepository constructor(private val backgroundManager: BackgroundManager,
                                       private val context: Context) {

    fun getBackgrounds() = backgroundManager.getBackgrounds(context)

}