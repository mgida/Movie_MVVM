package com.example.movie_mvvm.data.model

data class ReviewResponse(
    val id: Int,
    val page: Int,
    val results: List<ReviewModel>,
    val total_pages: Int,
    val total_results: Int
)


