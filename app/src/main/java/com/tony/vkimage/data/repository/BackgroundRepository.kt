package com.tony.vkimage.data.repository

import com.tony.vkimage.data.repository.datasource.BackgroundManager

class BackgroundRepository constructor(private val backgroundManager: BackgroundManager) {

    fun getBackgrounds() = backgroundManager.getBackgrounds()

}