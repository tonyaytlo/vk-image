package com.tony.vkimage.presentation.view.movigViewsLayout

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.tony.vkimage.R

class TrashFab @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val vibrator by lazy { context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    private val timeVibrate = 100L
    private var vibrate = true
    private val scaleOpen = 1.1F

    fun openTrash() {
        scaleX = scaleOpen
        scaleY = scaleOpen
        setImage(R.drawable.ic_fab_trash_released)
        shortVibrate()
    }

    fun closeTrash() {
        vibrate = false
        scaleX = 1F
        scaleY = 1F
        setImage(R.drawable.ic_fab_trash)
    }

    private fun setImage(resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setImageDrawable(resources.getDrawable(resId, context.theme))
        } else {
            this.setImageDrawable(resources.getDrawable(resId))
        }
    }

    private fun shortVibrate() {
        if (vibrate) { // one open trash - one vibrate event
            return
        }
        vibrator.cancel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(timeVibrate, VibrationEffect.CONTENTS_FILE_DESCRIPTOR))
        } else {
            vibrator.vibrate(timeVibrate)
        }
        vibrate = true
    }
}