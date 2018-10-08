package com.tony.vkimage.presentation.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tony.vkimage.R
import com.tony.vkimage.data.entity.Sticker
import com.tony.vkimage.presentation.StickerPickListener
import com.tony.vkimage.presentation.dialog.StickersBottomDialog
import ru.galt.app.extensions.bind


class MainActivity : AppCompatActivity(), StickerPickListener {

    private val toolbar by bind<Toolbar>(R.id.toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
    }

    private fun setupToolbar() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_font)
        toolbar.navigationIcon = drawable
        toolbar.title = ""
        setSupportActionBar(toolbar)
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

    //change font
        android.R.id.home -> {
            changeFont()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openStickersDialog() {
        val bottomSheetFragment = StickersBottomDialog()
        if (supportFragmentManager.findFragmentByTag(bottomSheetFragment.tag) == null) {
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun changeFont() {

    }

    override fun onStickerPicked(sticker: Sticker) {
        Log.d("MEINXX", sticker.imgPath)
    }
}

