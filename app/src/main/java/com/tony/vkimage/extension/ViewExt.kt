package com.tony.vkimage.extension

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.Toast
import java.util.*

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

fun Activity.postDelayed(action: () -> Unit, mls: Long = 0L) {
    Handler(Looper.getMainLooper()).postDelayed(action, mls)
}

inline fun View.onPreDraw(crossinline action: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
            action()
        }
    })
}

fun View.startFadeAnimation() {
    this.scaleX = 0F
    this.scaleY = 0F

    this.animate()
            .scaleX(1f)
            .scaleY(1f)
            .rotation(Random(System.currentTimeMillis()).nextInt(90) - 45F)
            .setDuration(300)
            .start()
}
