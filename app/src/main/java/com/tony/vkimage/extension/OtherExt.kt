package com.tony.vkimage.extension


val String.trimLine: String get() = this.replace("[\\n ]".toRegex(), "")

private val imagesExt = arrayOf("jpg", "png", "gif", "jpeg")

fun String.isImagePath(): Boolean {
    for (ext in imagesExt) {
        if (this.endsWith(ext, true)) {
            return true
        }
    }
    return false
}