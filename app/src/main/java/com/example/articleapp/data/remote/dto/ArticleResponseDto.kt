package com.example.articleapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponseDto(
    val articles: List<ArticleDto>
)
