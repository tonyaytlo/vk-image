package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Background.Background
import ru.galt.app.extensions.bind
import ru.galt.app.extensions.getColorRes

class BackgroundsAdapter constructor(private val context: Context, var data: MutableList<Background>,
                                     var onItemClick: ((Background) -> Unit)? = null,
                                     var onAddClick: (() -> Unit)? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_PLUS = 2
    }

    private val FOOTER_ITEM_PLUS = 1
    private val inflater
            by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
    private var selectedId: Int = -1
    private var selectedItemIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            if (viewType == VIEW_TYPE_ITEM)
                ItemHolder(inflater.inflate(R.layout.item_background, parent, false))
            else
                PlusHolder(inflater.inflate(R.layout.item_background, parent, false))

    override fun getItemViewType(position: Int): Int {
        return if (position != itemCount - 1) VIEW_TYPE_ITEM else VIEW_TYPE_PLUS
    }

    override fun getItemCount() = data.size + FOOTER_ITEM_PLUS

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.populateItem(data[position])
        } else if (holder is PlusHolder) {
            holder.populatePlusItem()
        }
    }

    fun onUnselect() {
        if (selectedItemIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedItemIndex)
            selectedId = -1
            selectedItemIndex - 1
        }
    }

    fun setSelected(id: Int, notify: Boolean = false) {
        val index = data.indexOfFirst { it.id == id }
        if (index == RecyclerView.NO_POSITION) {
            return
        }
        selectedId = id
        selectedItemIndex = index
        if (notify) {
            notifyItemChanged(selectedItemIndex)
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val siBackgrounds by bind<SelectableImageView>(R.id.siBackgrounds)

        init {
            itemView.setOnClickListener {
                if (selectedItemIndex == adapterPosition) {
                    return@setOnClickListener
                }
                val background = data[adapterPosition]
                siBackgrounds.setSelectedImage(true)

                selectedId = background.id
                notifyItemChanged(selectedItemIndex)
                selectedItemIndex = adapterPosition
                onItemClick?.invoke(background)
            }
        }

        fun populateItem(background: Background) {
            siBackgrounds.setSelectedImage(selectedId == background.id)
            Glide.with(this.itemView)
                    .load(background.getThumbnailDrawable())
                    .into(siBackgrounds)
        }
    }

    inner class PlusHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val siBackgrounds by bind<SelectableImageView>(R.id.siBackgrounds)

        init {
            itemView.setOnClickListener {
                onAddClick?.invoke()
            }
        }

        fun populatePlusItem() {
            siBackgrounds.setBackgroundColor(context.getColorRes(R.color.colorPlusBackground))
            Glide.with(this.itemView)
                    .load(R.drawable.ic_toolbar_new)
                    .into(siBackgrounds)

        }
    }
}