package com.example.articleapp.data.repository

import com.example.articleapp.data.remote.datasource.ArticleDataSource
import com.example.articleapp.data.remote.dto.ArticleResponseDto

class FakeArticleDataSource(
    private val response: ArticleResponseDto
): ArticleDataSource {
    override suspend fun getArticles(): ArticleResponseDto {
        return response
    }
}