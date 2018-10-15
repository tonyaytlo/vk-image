package com.tony.vkimage.presentation.view.bottomPanelView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Background
import com.tony.vkimage.presentation.view.SelectableImageView
import ru.galt.app.extensions.bind
import ru.galt.app.extensions.getColorRes

class BackgroundsAdapter constructor(private val context: Context, var data: MutableList<Background>,
                                     var onItemClick: ((Background) -> Unit)? = null,
                                     var onAddClick: (() -> Unit)? = null)
    : RecyclerView.Adapter<BackgroundsAdapter.Holder>() {

    private val FOOTER_ITEM_PLUS = 1
    private val inflater
            by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
    private var selectedId: Int = -1
    private var selectedItemIndex: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(inflater.inflate(R.layout.item_background, parent, false))

    override fun getItemCount() = data.size + FOOTER_ITEM_PLUS

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position < data.size) {
            holder.populate(data[position])
        } else {
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

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val siBackgrounds by bind<SelectableImageView>(R.id.siBackgrounds)

        init {
            itemView.setOnClickListener {
                if (adapterPosition == itemCount - 1) {
                    onAddClick?.invoke()
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

        fun populate(background: Background) {
            siBackgrounds.setSelectedImage(background.id == selectedId)
            Glide.with(this.itemView)
                    .load(background.thumbnailsId)
                    .into(siBackgrounds)
        }

        fun populatePlusItem() {
            siBackgrounds.setSelectedImage(false)
            siBackgrounds.setColorFilter(context.getColorRes(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN)
            Glide.with(this.itemView)
                    .load(R.drawable.ic_toolbar_new)
                    .into(siBackgrounds)

        }
    }
}