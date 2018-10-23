package com.tony.vkimage.presentation.task

import android.graphics.Bitmap
import android.os.AsyncTask
import com.tony.vkimage.presentation.interfaces.ImageSaveListener
import com.tony.vkimage.presentation.util.ImageHelper
import java.io.File
import java.lang.ref.WeakReference

class ImageSaveTask constructor(listener: ImageSaveListener, private val bitmap: Bitmap)
    : AsyncTask<Void, Void, File?>() {

    private var weakListener: WeakReference<ImageSaveListener> = WeakReference(listener)

    override fun doInBackground(vararg p0: Void?): File? {
        return ImageHelper.saveImageFromView(bitmap)
    }

    override fun onPostExecute(result: File?) {
        if (result != null) {
            weakListener.get()?.onSuccessSave(result)
        } else {
            weakListener.get()?.onErrorSave()
        }
    }
}