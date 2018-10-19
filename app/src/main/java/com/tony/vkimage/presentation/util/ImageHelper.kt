package com.tony.vkimage.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.view.View
import com.tony.vkimage.R
import com.tony.vkimage.VkApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageHelper {

    companion object {

        private const val QUALITY_DEFAULT = 80

        fun saveImageFromView(bitmap: Bitmap, quality: Int = QUALITY_DEFAULT): File? {
            val pictureFileDir = File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES), VkApp.appContext().getString(R.string.app_name))
            if (!pictureFileDir.exists()) {
                val isDirectoryCreated = pictureFileDir.mkdirs()
                if (!isDirectoryCreated)
                    return null
            }
            val filename = pictureFileDir.path + File.separator + System.currentTimeMillis() + ".jpg"
            val pictureFile = File(filename)

            try {
                pictureFile.createNewFile()
                val oStream = FileOutputStream(pictureFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, oStream)
                oStream.flush()
                oStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            scanGallery(VkApp.appContext(), pictureFile.absolutePath)
            return pictureFile
        }


        //Only in UI Thread or need block all action for view
        fun getBitmapFromView(view: View): Bitmap {
            val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(returnedBitmap)

            val bgDrawable = view.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            return returnedBitmap
        }

        private fun scanGallery(context: Context?, path: String) {
            try {
                MediaScannerConnection.scanFile(context, arrayOf(path), null) { _, _ -> }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}