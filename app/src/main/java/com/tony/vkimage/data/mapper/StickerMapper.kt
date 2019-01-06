package com.tony.vkimage.data.mapper

import com.tony.vkimage.data.entity.Sticker

class StickerMapper {

    companion object {
        private const val folderName = "stickers"
        private const val assetsPath = "file:///android_asset/$folderName"
    }

    fun mapEntity(strings: Array<String?>): MutableList<Sticker> {
        val stickers = mutableListOf<Sticker>()
        strings.forEach {
            if (it?.isNotEmpty() == true) {
                stickers.add(Sticker("$assetsPath/$it"))
            }
        }
        return stickers
    }
}