package com.tony.vkimage

import android.app.Application
import android.content.Context
import com.tony.vkimage.data.mapper.StickerMapper
import com.tony.vkimage.data.repository.BackgroundRepository
import com.tony.vkimage.data.repository.StickerRepository
import com.tony.vkimage.data.repository.datasource.BackgroundManager
import com.tony.vkimage.data.repository.datasource.StickerManager


class VkApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: VkApp? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }

        val stickerRepository by lazy { StickerRepository(StickerManager(), StickerMapper()) }
        val backgroundRepository by lazy { BackgroundRepository(BackgroundManager(), VkApp.appContext()) }
    }
}