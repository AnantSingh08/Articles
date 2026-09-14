package com.example.articleapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ArticleRoute {
    @Serializable
    data object Articles: ArticleRoute()

    @Serializable
    data class Details(
        val articleId: Long
    ): ArticleRoute()
}