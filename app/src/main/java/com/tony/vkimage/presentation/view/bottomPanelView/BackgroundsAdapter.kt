package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.BackgroundDrawable
import com.tony.vkimage.extension.bind
import com.tony.vkimage.extension.getColorRes

class BackgroundsAdapter constructor(private val context: Context,
                                     var data: MutableList<BackgroundDrawable>,
                                     var onItemClick: ((BackgroundDrawable) -> Unit)? = null,
                                     var onAddClick: (() -> Unit)? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val NO_ID = -1

        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_PLUS = 2

        private const val PAYLOAD_SELECTION = "selection"
    }

    private val inflater
            by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }

    private val ITEM_FOOTER_PLUS = 1

    private var selectedId: Int = NO_ID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            if (viewType == VIEW_TYPE_ITEM)
                ItemHolder(inflater.inflate(R.layout.item_background, parent, false))
            else
                PlusHolder(inflater.inflate(R.layout.item_background, parent, false))

    override fun getItemViewType(position: Int) =
            if (position != itemCount - 1) VIEW_TYPE_ITEM else VIEW_TYPE_PLUS

    override fun getItemCount() =
            data.size + ITEM_FOOTER_PLUS

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.populateItem(data[position])
        } else if (holder is PlusHolder) {
            holder.populatePlusItem()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (holder is ItemHolder) {
            if (payloads.size > 0) {
                holder.bindSelect(data[position])
                return
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun unselectItem() {
        if (selectedId == NO_ID) {
            return
        }
        val index = data.indexOfFirst { it.id == selectedId }
        notifyItemChanged(index)
        selectedId = NO_ID
    }

    fun selectItem(id: Int, notify: Boolean = true, position: Int = RecyclerView.NO_POSITION) {
        if (selectedId == id) {
            return
        }

        val posLast = data.indexOfFirst { it.id == selectedId }
        val pos = if (position == RecyclerView.NO_POSITION) {
            data.indexOfFirst { it.id == id }
        } else {
            position
        }

        selectedId = id

        if (notify) {
            notifyItemChanged(posLast, PAYLOAD_SELECTION)
            notifyItemChanged(pos, PAYLOAD_SELECTION)
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val siBackgrounds by bind<SelectableImageView>(R.id.siBackgrounds)

        init {
            itemView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                selectItem(selectedId, position = adapterPosition)
                onItemClick?.invoke(data[adapterPosition])
            }
        }

        fun populateItem(backgroundDrawable: BackgroundDrawable) {
            bindSelect(backgroundDrawable)
            Glide.with(this.itemView)
                    .load(backgroundDrawable.getThumbnailDrawable())
                    .into(siBackgrounds)
        }

        fun bindSelect(backgroundDrawable: BackgroundDrawable) {
            siBackgrounds.setSelectedImage(selectedId == backgroundDrawable.id)
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