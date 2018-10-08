package com.tony.vkimage.util

import android.content.Context
import android.graphics.drawable.Drawable
import java.io.IOException
import java.io.InputStream

class StickerManager {

    fun getStrickers(context: Context): Array<Drawable?> {

        var inputStream: InputStream?
        var drawables: Array<Drawable?> = arrayOfNulls(0)

        try {
            val folderName = "stickers"
            val assetManager = context.assets

            val images = assetManager.list(folderName)

            drawables = arrayOfNulls(images.size)
            for (i in images.indices) {
                inputStream = assetManager.open("$folderName/${images[i]}")
                val drawable = Drawable.createFromStream(inputStream, null)
                drawables[i] = drawable
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return drawables
    }
}