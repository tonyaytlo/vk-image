package com.tony.vkimage.presentation.adapter

import android.support.v7.widget.RecyclerView

abstract class SelectableAdapter<VH : RecyclerView.ViewHolder, I>(val data: MutableList<I>) : RecyclerView.Adapter<VH>() {

    companion object {
        private const val NO_ID = -1
        private const val PAYLOAD_UNSELECT = 0
        private const val PAYLOAD_SELECT = 1
    }

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
        if (posPrev != RecyclerView.NO_POSITION) {
            notifyItemChanged(posPrev, PAYLOAD_UNSELECT)
        }
        if (pos != NO_ID) {
            notifyItemChanged(posPrev, PAYLOAD_SELECT)
        }
    }


}