package com.tony.vkimage.data.entity

data class Background constructor(val drawableId: Int, val thumbnailsId: Int, val id: Int = id_) {
    companion object {
        private var id_ = 0
            get() {
                return field++
            }
    }
}