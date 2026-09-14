package com.example.articleapp.data.remote.api

import com.example.articleapp.data.remote.dto.ArticleResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArticleApi(
    private val client: HttpClient
) {
    suspend fun getArticles(): ArticleResponseDto {
        return client.get("http://10.0.2.2:8080/mock/articlesApp/articles").body()
    }
}