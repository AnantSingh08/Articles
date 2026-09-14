package com.example.articleapp.data.remote.datasource

import com.example.articleapp.data.remote.dto.ArticleResponseDto

interface ArticleDataSource {
    suspend fun getArticles(): ArticleResponseDto
}