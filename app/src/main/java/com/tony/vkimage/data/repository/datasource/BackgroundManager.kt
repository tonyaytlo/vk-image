package com.tony.vkimage.data.repository.datasource

import android.content.Context
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.BackgroundDrawable
import com.tony.vkimage.data.entity.BackgroundColor
import com.tony.vkimage.data.entity.BackgroundRes

class BackgroundManager {

    fun getBackgrounds(context: Context): MutableList<BackgroundDrawable> {
        val background = mutableListOf<BackgroundDrawable>()

        val colors = context.resources.getIntArray(R.array.colors_background)
        for (i in 0 until colors.size step 2) {
            background.add(BackgroundColor(intArrayOf(colors[i], colors[i + 1])))
        }

        background.add(BackgroundRes(R.drawable.bg_beach, R.drawable.thumb_beach))
        background.add(BackgroundRes(R.drawable.bg_stars_center, R.drawable.thumb_stars))

        return background
    }

}