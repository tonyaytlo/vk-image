package com.tony.vkimage.presentation.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tony.vkimage.presentation.adapter.StickerAdapter
import com.tony.vkimage.R
import com.tony.vkimage.VkApp
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.presentation.interfaces.StickerPickListener
import com.tony.vkimage.extension.bind


class StickersBottomDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "StickersBottomDialog"

        private const val SPAN_COUNT = 4
    }

    private val rvStickers by bind<RecyclerView>(R.id.rvStickers)

    private var stickerPickListener: StickerPickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is StickerPickListener) {
            stickerPickListener = activity as StickerPickListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_dialog_stickers, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getStickers()
    }

    private fun initRecycler() {
        rvStickers.setHasFixedSize(true)
        rvStickers.layoutManager = GridLayoutManager(activity, SPAN_COUNT)
    }

    private fun getStickers() {
        val stickers = VkApp.stickerRepository.getStickers(activity!!.applicationContext)
        showStickers(stickers ?: mutableListOf())
    }

    private fun showStickers(stickers: MutableList<Sticker>) {
        val adapter = StickerAdapter(activity!!.applicationContext, stickers) { sticker ->
            stickerPickListener?.onStickerPicked(sticker)
            dismiss()
        }
        rvStickers.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        stickerPickListener = null
    }
}