package com.tony.vkimage.data.repository.datasource

import android.content.Context
import java.io.IOException

class StickerManager {

    fun getStickers(context: Context) = readAssetsFiles(context, "stickers")

    private fun readAssetsFiles(context: Context, folderName: String): Array<String?> {
        var images = emptyArray<String?>()
        try {
            val assetManager = context.assets
            images = assetManager.list(folderName)
            //assetManager.open("$folderName/${images[i]}")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return images
    }
}