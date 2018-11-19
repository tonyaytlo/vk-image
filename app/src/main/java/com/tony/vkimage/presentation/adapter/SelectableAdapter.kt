package com.tony.vkimage.presentation.adapter

import android.support.v7.widget.RecyclerView

abstract class SelectableAdapter<VH : RecyclerView.ViewHolder, I>(val data: MutableList<I>) : RecyclerView.Adapter<VH>() {

//    companion object {
//        private const val NO_ID
//    }

    private var selectedItem: I? = null

    private fun selectItem(item: I) {
        if (item == selectedItem) {
            return
        }

        val pos = data.indexOfFirst { it == item }
        var posPrev = RecyclerView.NO_POSITION
        if (selectedItem != null) {
            posPrev = data.indexOfFirst { it == selectedItem }
        }
    }


}