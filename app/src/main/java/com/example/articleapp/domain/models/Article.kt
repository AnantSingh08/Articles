package com.example.articleapp.domain.models

data class Article(
    val id: Long,
    val title: String,
    val description: String,
    val isBookmarked: Boolean,
    val imageUrl: String?,
)
