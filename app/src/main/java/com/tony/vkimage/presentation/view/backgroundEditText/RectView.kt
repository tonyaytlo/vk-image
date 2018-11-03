package com.tony.vkimage.presentation.view.backgroundEditText

import android.os.Parcel
import android.os.Parcelable

data class RectView constructor(var width: Int = 0, var height: Int = 0,
                                var left: Int = 0, var top: Int = 0,
                                var right: Int = 0, var bottom: Int = 0) : Parcelable {

    override fun writeToParcel(out: Parcel?, p1: Int) {
        out!!.writeInt(width)
        out.writeInt(height)
        out.writeInt(left)
        out.writeInt(top)
        out.writeInt(right)
        out.writeInt(bottom)
    }

    fun readFromParcel(`in`: Parcel) {
        width = `in`.readInt()
        height = `in`.readInt()
        left = `in`.readInt()
        top = `in`.readInt()
        right = `in`.readInt()
        bottom = `in`.readInt()
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RectView> {
        override fun createFromParcel(parcel: Parcel): RectView {
            val newRectView = RectView()
            newRectView.readFromParcel(parcel)
            return newRectView
        }

        override fun newArray(size: Int): Array<RectView?> {
            return arrayOfNulls(size)
        }
    }
}