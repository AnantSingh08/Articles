package com.example.articleapp.data.remote.datasource

import com.example.articleapp.data.remote.api.ArticleApi
import com.example.articleapp.data.remote.dto.ArticleResponseDto

class KtorArticleDataSource(
    private val api: ArticleApi
) : ArticleDataSource {
    override suspend fun getArticles(): ArticleResponseDto {
        return api.getArticles()
    }
}