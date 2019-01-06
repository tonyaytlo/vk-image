package com.tony.vkimage.presentation.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.extension.bind
import com.tony.vkimage.extension.loadImageFromUri


class StickerAdapter constructor(private val context: Context, var data: MutableList<Sticker>,
                                 var onItemClick: ((Sticker) -> Unit)? = null)
    : RecyclerView.Adapter<StickerAdapter.StickerHolder>() {

    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            StickerHolder(inflater.inflate(R.layout.item_sticker, parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: StickerHolder, position: Int) {
        holder.populate(data[position])
    }

    inner class StickerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImage by bind<ImageView>(R.id.ivSticker)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition])
            }
        }

        fun populate(sticker: Sticker) {
            ivImage.loadImageFromUri(Uri.parse(sticker.imgPath))
        }
    }
}