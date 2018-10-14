package com.tony.vkimage

import android.app.Application
import com.tony.vkimage.data.repository.datasource.StickerManager
import com.tony.vkimage.data.repository.StickerRepository
import com.tony.vkimage.data.mapper.StickerMapper

class NewsApp : Application() {

    companion object {
        val stickerRepository by lazy { StickerRepository(StickerManager(), StickerMapper()) }
    }

    override fun onCreate() {
        super.onCreate()
    }
}