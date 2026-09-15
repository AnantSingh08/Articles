package com.example.articleapp.domain.repository

import com.example.articleapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getAllArticles(): Flow<List<Article>>

    suspend fun refreshArticles()

    suspend fun updateBookmark(
        articleId: Long,
        isBookmarked: Boolean
    )
}