package com.tony.vkimage.data.repository

import android.content.Context
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.data.mapper.StickerMapper
import com.tony.vkimage.data.repository.datasource.StickerManager

class StickerRepository constructor(private val stickerManager: StickerManager,
                                    private val stickerMapper: StickerMapper) {

    private var stickers: MutableList<Sticker>? = null

    fun getStickers(context: Context): MutableList<Sticker>? {
        if (stickers == null) {
            return stickerMapper.mapEntity(stickerManager.getStickers(context))
        }
        return stickers
    }
}