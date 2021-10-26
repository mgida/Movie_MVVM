package com.example.movie_mvvm.data.model.trailer

import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    val id: Int,
    @SerializedName("results")
    val trailers: List<TrailerModel>
)