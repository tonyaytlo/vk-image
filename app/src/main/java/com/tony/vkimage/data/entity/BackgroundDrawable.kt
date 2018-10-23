package com.tony.vkimage.data.entity

import android.graphics.drawable.Drawable

abstract class BackgroundDrawable constructor(val id: Int = id_) {

    companion object {
        private var id_ = 0
            get() {
                return field++
            }
    }

    abstract fun getDrawable(): Drawable?

    abstract fun getThumbnailDrawable(): Drawable?
}