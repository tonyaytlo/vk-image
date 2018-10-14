package ru.galt.app.extensions

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun AppCompatActivity.hideKeyboard() {
    if (currentFocus != null) {
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Context.getColorRes(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}
