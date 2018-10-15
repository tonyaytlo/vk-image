package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.Button
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Background
import ru.galt.app.extensions.bind

class BottomPanelView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val rvBackground by bind<RecyclerView>(R.id.rvBackgrounds)
    private val btnSave by bind<Button>(R.id.btnSave)
    private var adapter: BackgroundsAdapter? = null

    private var onItemClick: ((Background) -> Unit)? = null
    private var onAddClick: (() -> Unit)? = null

    init {
        inflate(context, R.layout.bottom_panel_view, this)
        setRecycler()
    }

    private fun setRecycler() {
        rvBackground.setHasFixedSize(true)
        rvBackground.itemAnimator = null // disable default animation
        rvBackground.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun showBackground(data: MutableList<Background>) {
        adapter = BackgroundsAdapter(context.applicationContext, data, onItemClick, onAddClick)
        rvBackground.adapter = adapter
    }

    fun getSaveBtn() = btnSave

    fun setOnItemClickListener(onItemClick: ((Background) -> Unit)? = null) {
        this.onItemClick = onItemClick
    }

    fun setOnAddClickListener(onAddClick: (() -> Unit)? = null) {
        this.onAddClick = onAddClick
    }

    fun unselectBackground() {
        adapter?.onUnselect()
    }

}