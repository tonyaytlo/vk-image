package com.tony.vkimage.extension

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun View.setVisibility(visible: Boolean) {
    if (visible) makeVisible() else makeGone()
}

fun Context.showToast(text: String) {
    showToast(this, text)
}

fun showToast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, length).show()
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun EditText.hideCursor() {
    isCursorVisible = false
}

fun EditText.showCursor() {
    isCursorVisible = true
}
