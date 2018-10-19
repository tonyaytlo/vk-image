package com.tony.vkimage.data.entity.Background

import android.graphics.drawable.Drawable

abstract class Background constructor(val id: Int = id_) {

    companion object {
        private var id_ = 0
            get() {
                return field++
            }
    }

    abstract fun getDrawable(): Drawable?

    abstract fun getThumbnailDrawable(): Drawable?
}