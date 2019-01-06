package com.tony.vkimage.extension


val String.trimLine: String get() = this.replace("[\\n ]".toRegex(), "")

fun String.isImagePath(): Boolean {
    for (ext in imagesExt) {
        if (this.endsWith(ext, true)) {
            return true
        }
    }
    return false
}

private val imagesExt = arrayOf("jpg", "png", "gif", "jpeg")
