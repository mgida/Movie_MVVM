package com.example.movie_mvvm.data.model.cast

data class CastResponse(
    val cast: MutableList<CastModel>,
    val crew: List<CrewModel>,
    val id: Int
)