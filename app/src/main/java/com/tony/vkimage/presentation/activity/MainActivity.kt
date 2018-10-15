package com.tony.vkimage.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tony.tinkoffnews.extension.showToast
import com.tony.vkimage.R
import com.tony.vkimage.VkApp
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.extension.isImagePath
import com.tony.vkimage.presentation.StickerPickListener
import com.tony.vkimage.presentation.dialog.StickersBottomDialog
import com.tony.vkimage.presentation.view.bottomPanelView.BottomPanelView
import com.tony.vkimage.presentation.view.customEditText.CustomEditText
import com.tony.vkimage.presentation.view.movigViewsLayout.MovingViewsLayout
import ru.galt.app.extensions.*


class MainActivity : AppCompatActivity(), StickerPickListener {


    private val root by bind<ViewGroup>(R.id.root)
    private val toolbar by bind<Toolbar>(R.id.toolbar)
    private val flImage by bind<View>(R.id.flImage)
    private val ivBackground by bind<ImageView>(R.id.ivBackground)
    private val rlContainer by bind<MovingViewsLayout>(R.id.rlContainer)
    private val etStoryText by bind<CustomEditText>(R.id.etStoryText)
    private val bpPanel by bind<BottomPanelView>(R.id.bpPanel)

    private val REQUEST_PERMISSIONS = 121
    private val REQUEST_IMAGE = 122
    private val etStylesBackground = intArrayOf(
            CustomEditText.STYLE_ROUND_RECT_BACKGROUND,
            CustomEditText.STYLE_RECT_BACKGROUND,
            CustomEditText.STYLE_TRANSPARENT_BACKGROUND)
    private var etStyleIndex = 0

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setOnLayoutListener()
        setViewListeners()
        showBackgrounds()
    }

    private fun setOnLayoutListener() {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rootHeight = root.rootView.height
            val keyboardHeight = rootHeight - root.height
            if (keyboardHeight > 150) {
                val possibleHeight = rootHeight - keyboardHeight - toolbar.height
                flImage.layoutParams = flImage.layoutParams.apply {
                    height = possibleHeight
                }
                root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)// CHECK CASE (MIN HEIGHT)
            }
        }
        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun setupToolbar() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_font)
        toolbar.navigationIcon = drawable
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    private fun setViewListeners() {
        bpPanel.getSaveBtn().setOnClickListener { saveImage() }
        bpPanel.setOnAddClickListener { openGallery() }
        bpPanel.setOnItemClickListener {
            Glide.with(this)
                    .load(it.drawableId)
                    .into(ivBackground)
        }
    }

    private fun saveImage() {
        if (VkApp.imageHelper.saveImageFromView(this, flImage) != null) {
            showToast(getString(R.string.image_saved))
        } else {
            showToast(getString(R.string.error_image_save))
        }
    }

    private fun showBackgrounds() {
        bpPanel.showBackground(VkApp.backgroundRepository.getBackgrounds())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_stickers -> {
            openStickersDialog()
            true
        }

        android.R.id.home -> {
            changeFontStyle()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun openStickersDialog() {
        hideKeyboard()
        val bottomSheetFragment = StickersBottomDialog()
        if (supportFragmentManager.findFragmentByTag(StickersBottomDialog.TAG) == null) {
            bottomSheetFragment.show(supportFragmentManager, StickersBottomDialog.TAG)
        }
    }

    private fun changeFontStyle() {
        etStyleIndex++
        if (etStyleIndex >= etStylesBackground.size) {
            etStyleIndex = 0
        }
        etStoryText.setStyle(etStylesBackground[etStyleIndex])
    }

    override fun onStickerPicked(sticker: Sticker) {
        addSticker(sticker)
    }

    private fun addSticker(sticker: Sticker) {
        val ivSticker = ImageView(this)
        ivSticker.layoutParams = MovingViewsLayout.LayoutParams(ViewGroup.LayoutParams(120.dpToPx, 120.dpToPx))
        ivSticker.id = View.generateViewId()
        rlContainer.addView(ivSticker)
        Glide.with(this)
                .load(Uri.parse(sticker.imgPath))
                .into(ivSticker)
    }

    private fun openGallery() {
        hideKeyboard()
        openGalleryIntent(REQUEST_IMAGE, REQUEST_PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                if (data != null) {
                    val selectedImageUri = data.data
                    val path = getPath(selectedImageUri) ?: selectedImageUri.path
                    if (path.isImagePath()) {
                        bpPanel.unselectBackground()
                        Glide.with(this)
                                .load(path)
                                .apply(RequestOptions().centerCrop())
                                .into(ivBackground)
                    } else {
                        showToast(getString(R.string.error_image_type))
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isPermissionsGranted()) {
                openGallery()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}

