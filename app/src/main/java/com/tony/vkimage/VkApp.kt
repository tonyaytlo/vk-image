package com.tony.vkimage

import android.app.Application
import com.tony.vkimage.data.mapper.StickerMapper
import com.tony.vkimage.data.repository.BackgroundRepository
import com.tony.vkimage.data.repository.StickerRepository
import com.tony.vkimage.data.repository.datasource.BackgroundManager
import com.tony.vkimage.data.repository.datasource.StickerManager
import com.tony.vkimage.presentation.ImageHelper


class VkApp : Application() {

    companion object {
        val stickerRepository by lazy { StickerRepository(StickerManager(), StickerMapper()) }
        val backgroundRepository by lazy { BackgroundRepository(BackgroundManager()) }
        val imageHelper by lazy { ImageHelper() }
    }

    override fun onCreate() {
        super.onCreate()
    }
}