package ru.galt.app.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dpToPxF: Float get() = (this / Resources.getSystem().displayMetrics.density)

fun AppCompatActivity.hideKeyboard() {
    if (currentFocus != null) {
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun IntArray.isPermissionsGranted(): Boolean {
    for (permission in this) {
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}


fun Context.getColorRes(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}
