package com.tony.vkimage.presentation.interfaces

import java.io.File

interface ImageSaveListener {

    fun onSuccessSave(file: File)
    fun onErrorSave()
}