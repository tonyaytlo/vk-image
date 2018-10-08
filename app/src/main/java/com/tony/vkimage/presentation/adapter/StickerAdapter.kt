package com.tony.tinkoffnews.presentation.view.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Sticker
import ru.galt.app.extensions.bind


class StickerAdapter constructor(private val context: Context, var data: MutableList<Sticker>,
                                 var onItemClick: ((Sticker) -> Unit)? = null)
    : RecyclerView.Adapter<StickerAdapter.Holder>() {

    private val inflater
            by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(inflater.inflate(R.layout.item_sticker, parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.populate(data[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImage by bind<ImageView>(R.id.ivSticker)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition])
            }
        }

        fun populate(sticker: Sticker) {
            Glide.with(itemView)
                    .load(Uri.parse(sticker.imgPath))
                    .into(ivImage)
        }
    }
}