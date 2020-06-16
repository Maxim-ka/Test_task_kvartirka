package com.reschikov.kvartirka.testtask.data.network.model

import android.os.Parcel
import android.os.Parcelable

private const val EMPTY_STRING =""

data class Photo(val url : String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: EMPTY_STRING)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}