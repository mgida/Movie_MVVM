package com.example.movie_mvvm.data.model.trailer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class TrailerModel(
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val site: String,
    val size: Int,
    val type: String
) : Parcelable