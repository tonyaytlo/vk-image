package com.tony.vkimage.data.mapper

import com.tony.vkimage.data.entity.Sticker

class StickerMapper {

    fun mapEntity(strings: Array<String?>): MutableList<Sticker> {
        val stickers = mutableListOf<Sticker>()
        strings.forEach {
            if (it != null) {
                stickers.add(Sticker("file:///android_asset/stickers/${it}"))
            }
        }
        return stickers
    }
}