package com.tony.vkimage.extension

import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.RawRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageFromUrl(url: String?, requestOptions: RequestOptions) {
    Glide.with(this.context.applicationContext)
            .load(url)
            .apply(requestOptions)
            .into(this)
}

fun ImageView.loadImageFromUri(uri: Uri?) {
    Glide.with(this.context.applicationContext)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}


fun ImageView.loadImageFromDrawable(drawable: Drawable?) {
    Glide.with(this.context.applicationContext)
            .load(drawable)
            .into(this)
}

fun ImageView.loadImageFromDrawableWithTransition(drawable: Drawable?) {
    Glide.with(this.context.applicationContext)
            .load(drawable)
            .transition(DrawableTransitionOptions.withCrossFade(400))
            .into(this)
}

fun ImageView.loadImageFromDrawableRes(@RawRes @DrawableRes resourceId: Int?) {
    Glide.with(this.context.applicationContext)
            .load(resourceId)
            .into(this)
}