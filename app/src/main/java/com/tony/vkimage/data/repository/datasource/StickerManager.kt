package com.tony.vkimage.data.repository.datasource

import android.content.Context
import java.io.IOException

class StickerManager {

    fun getStickers(context: Context) = readAssetsFiles(context, "stickers")

    private fun readAssetsFiles(context: Context, folderName: String): Array<String?> {
        // context = context.applicationContext
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

//    //only test - wrong way
//    fun getStickers(context: Context): Array<Drawable?> {
//
//        var inputStream: InputStream?
//        var drawables: Array<Drawable?> = arrayOfNulls(0)
//
//        try {
//            val folderName = "stickers"
//            val assetManager = context.assets
//
//            val images = assetManager.list(folderName)
//
//            drawables = arrayOfNulls(images.size)
//            for (i in images.indices) {
//                inputStream = assetManager.open("$folderName/${images[i]}")
//                val drawable = Drawable.createFromStream(inputStream, null)
//                drawables[i] = drawable
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return drawables
//    }
//
}