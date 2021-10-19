package com.example.movie_mvvm.data.model

data class ReviewModel(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)
data class AuthorDetails(
    val avatar_path: String,
    val name: String,
    val rating: Any,
    val username: String
)