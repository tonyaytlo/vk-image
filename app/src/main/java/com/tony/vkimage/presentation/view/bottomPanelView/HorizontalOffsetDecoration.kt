package com.tony.vkimage.presentation.view.bottomPanelView

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class HorizontalOffsetDecoration(val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = (view?.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition == 0) {
            outRect?.left = offset
        }
        outRect?.right = offset
    }
}