package com.tony.vkimage.presentation.interfaces

import com.tony.vkimage.data.entity.Sticker

interface StickerPickListener {
    fun onStickerPicked(sticker: Sticker)
}