package com.tony.vkimage.presentation.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.tony.vkimage.R
import com.tony.vkimage.VkApp
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.extension.*
import com.tony.vkimage.presentation.dialog.StickersBottomDialog
import com.tony.vkimage.presentation.interfaces.ImageSaveListener
import com.tony.vkimage.presentation.interfaces.StickerPickListener
import com.tony.vkimage.presentation.task.ImageSaveTask
import com.tony.vkimage.presentation.util.ImageHelper
import com.tony.vkimage.presentation.view.backgroundEditText.BackgroundEditText
import com.tony.vkimage.presentation.view.bottomPanelView.BottomPanelView
import com.tony.vkimage.presentation.view.movigViewsLayout.MovingViewsLayout
import java.io.File


class MainActivity : AppCompatActivity(), StickerPickListener, ImageSaveListener {

    companion object {
        private const val REQUEST_PERMISSIONS_GALLERY = 1
        private const val REQUEST_IMAGE = 2
        private const val REQUEST_IMAGE_SAVE = 3
    }

    private val root by bind<ViewGroup>(R.id.root)
    private val toolbar by bind<Toolbar>(R.id.toolbar)
    private val flImage by bind<View>(R.id.flImage)
    private val ivBackground by bind<ImageView>(R.id.ivBackground)
    private val mvMovingContainer by bind<MovingViewsLayout>(R.id.rlContainer)
    private val etStoryText by bind<BackgroundEditText>(R.id.etStoryText)
    private val bpPanel by bind<BottomPanelView>(R.id.bpPanel)

    private val etStylesBackground = intArrayOf(
            BackgroundEditText.STYLE_ROUND_RECT_BACKGROUND,
            BackgroundEditText.STYLE_RECT_BACKGROUND,
            BackgroundEditText.STYLE_TRANSPARENT_BACKGROUND)
    private var etStyleIndex = 0

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var isKeyboardVisible = false
    private var isFirstOpen = true
    private val glideRequestOptions by lazy {
        RequestOptions().override(700, 500).centerCrop()
    }

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
            val rootHeight = root.rootView.height //screen height
            val keyboardHeight = rootHeight - root.height
            val isKeyboardNowVisible = keyboardHeight > rootHeight * 0.15

            if (isKeyboardVisible != isKeyboardNowVisible) {
                if (isKeyboardNowVisible) {
                    if (isFirstOpen) {
                        setBackgroundSize(rootHeight - keyboardHeight)
                    }
                    etStoryText.showCursor()
                } else {
                    etStoryText.hideCursor()
                }
            }
            isKeyboardVisible = isKeyboardNowVisible
        }
        root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun setupToolbar() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_font)
        toolbar.navigationIcon = drawable
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    private fun setBackgroundSize(posHeight: Int) {
        val possibleHeight = posHeight - toolbar.height - bpPanel.height
        flImage.layoutParams = flImage.layoutParams.apply {
            height = possibleHeight
        }
    }

    private fun setViewListeners() {
        etStoryText.setOnClickListener { etStoryText.showCursor() }
        bpPanel.getSaveButton().setOnClickListener { saveImage() }
        bpPanel.setOnAddClickListener { openGallery() }
        bpPanel.setOnItemClickListener {
            ivBackground.loadImageFromDrawableWithTransition(it.getDrawable())
        }
        ivBackground.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                if (!isKeyboardVisible) {
                    etStoryText.showKeyboard()
                }
            }
            false
        }
    }

    private fun saveImage() {
        if (isPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            hideKeyboard()
            etStoryText.hideCursor()
            val imageSaveTask = ImageSaveTask(this, ImageHelper.getBitmapFromView(flImage))
            imageSaveTask.execute()
        } else {
            askPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_IMAGE_SAVE)
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

        R.id.action_text_visibility -> {
            toggleTextVisibility()
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
        if (supportFragmentManager.findFragmentByTag(StickersBottomDialog.TAG) == null) {
            StickersBottomDialog().show(supportFragmentManager, StickersBottomDialog.TAG)
        }
    }

    private fun changeFontStyle() {
        etStyleIndex = (etStyleIndex + 1) % etStylesBackground.size
        etStoryText.setBackgroundStyle(etStylesBackground[etStyleIndex])
    }

    private fun toggleTextVisibility() {
        etStoryText.setVisibility(etStoryText.visibility != View.VISIBLE)
        if (etStoryText.visibility == View.GONE) {
            hideKeyboard()
        }
    }

    override fun onStickerPicked(sticker: Sticker) {
        postDelayed({ addSticker(sticker) }, 75)
    }

    private fun addSticker(sticker: Sticker) {
        val size = 120.dpToPx
        val ivSticker = ImageView(this)
        ivSticker.layoutParams =
                MovingViewsLayout.LayoutParams(ViewGroup.LayoutParams(size, size))
        ivSticker.id = View.generateViewId()

        mvMovingContainer.addView(ivSticker)
        ivSticker.onPreDraw {
            ivSticker.startFadeAnimation()
        }
        ivSticker.loadImageFromUri(Uri.parse(sticker.imgPath))
    }

    override fun onSuccessSave(file: File) {
        showToast(getString(R.string.image_saved))
    }

    override fun onErrorSave() {
        showToast(getString(R.string.error_image_save))
    }

    private fun openGallery() {
        hideKeyboard()
        openGalleryIntent(REQUEST_IMAGE, REQUEST_PERMISSIONS_GALLERY)
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
                        ivBackground.loadImageFromUrl(path, glideRequestOptions)
                    } else {
                        showToast(getString(R.string.error_image_type))
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (requestCode == REQUEST_PERMISSIONS_GALLERY && results.isPermissionsGranted()) {
            openGallery()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        root.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}

