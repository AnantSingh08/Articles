package com.example.articleapp.data

import com.example.articleapp.data.local.ArticleEntity
import com.example.articleapp.data.remote.dto.ArticleDto
import com.example.articleapp.domain.models.Article

fun ArticleEntity.toDomain(): Article {
    return Article(
        id = id,
        title = title,
        description = description,
        isBookmarked = isBookmarked,
        imageUrl = imageUrl
    )
}

fun ArticleDto.toEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        description = description,
        isBookmarked = false,
        imageUrl = imageUrl
    )
}