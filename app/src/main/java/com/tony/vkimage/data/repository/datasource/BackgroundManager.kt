package com.tony.vkimage.data.repository.datasource

import android.content.Context
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Background.Background
import com.tony.vkimage.data.entity.Background.BackgroundColor
import com.tony.vkimage.data.entity.Background.BackgroundRes


class BackgroundManager {

    fun getBackgrounds(context: Context): MutableList<Background> {
        val background = mutableListOf<Background>()

        val colors = context.resources.getIntArray(R.array.colors_background)
        for (i in 0 until colors.size step 2) {
            background.add(BackgroundColor(intArrayOf(colors[i], colors[i + 1])))
        }
        background.add(BackgroundRes(R.drawable.bg_beach, R.drawable.thumb_beach))
        background.add(BackgroundRes(R.drawable.bg_stars_center, R.drawable.thumb_stars))

        return background
    }

}