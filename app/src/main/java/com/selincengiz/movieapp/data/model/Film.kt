package com.selincengiz.movieapp.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val description: String,
    val isWatched: Boolean,
    val date: String,
    val image: Bitmap?
) : Parcelable
