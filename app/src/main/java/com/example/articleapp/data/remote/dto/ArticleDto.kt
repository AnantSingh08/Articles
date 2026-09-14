package com.example.articleapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val isBookmarked: Boolean = false
)
