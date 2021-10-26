package com.example.movie_mvvm.data.model.review

data class ReviewResponse(
    val id: Int,
    val page: Int,
    val results: MutableList<ReviewModel>,
    val total_pages: Int,
    val total_results: Int
)


