package com.example.articleapp.ui.uiStates

import com.example.articleapp.domain.models.Article

sealed interface ArticleUiState {

    data object Loading: ArticleUiState

    data class Success(
        val articles: List<Article>
    ): ArticleUiState

    data class Error(
        val message: String
    ): ArticleUiState
}