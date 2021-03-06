package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.Button
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.BackgroundDrawable
import com.tony.vkimage.extension.bind

class BottomPanelView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val rvBackground by bind<RecyclerView>(R.id.rvBackgrounds)
    private val btnSave by bind<Button>(R.id.btnSave)
    private var adapter: BackgroundsAdapter? = null

    private var onItemClick: ((BackgroundDrawable) -> Unit)? = null
    private var onAddClick: (() -> Unit)? = null

    init {
        inflate(context, R.layout.bottom_panel_view, this)
        setRecycler()
    }

    private fun setRecycler() {
        rvBackground.setHasFixedSize(true)
        rvBackground.itemAnimator = null // disable default animation
        rvBackground.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBackground.addItemDecoration(HorizontalOffsetDecoration())
    }

    fun showBackground(data: MutableList<BackgroundDrawable>) {
        adapter = BackgroundsAdapter(context.applicationContext, data, onItemClick, onAddClick)
        // set default selected
        val background = data.firstOrNull()
        if (background != null) {
            adapter?.setSelected(background.id)
            onItemClick?.invoke(background)
        }
        rvBackground.adapter = adapter
    }

    fun getSaveButton() = btnSave

    fun setOnItemClickListener(onItemClick: ((BackgroundDrawable) -> Unit)? = null) {
        this.onItemClick = onItemClick
    }

    fun setOnAddClickListener(onAddClick: (() -> Unit)? = null) {
        this.onAddClick = onAddClick
    }

    fun unselectBackground() {
        adapter?.onUnselect()
    }

}