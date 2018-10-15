package com.tony.vkimage.data.repository.datasource

import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Background

class BackgroundManager {

    fun getBackgrounds(): MutableList<Background> {
        val background = mutableListOf<Background>()
        background.add(Background(R.drawable.bg_beach, R.drawable.thumb_beach))
        background.add(Background(R.drawable.bg_stars_center, R.drawable.thumb_stars))
        return background
    }

}