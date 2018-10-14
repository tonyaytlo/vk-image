package com.tony.vkimage.extension

val String.trimLine: String
    get() = this
            .replace("[\\n ]".toRegex(), "")