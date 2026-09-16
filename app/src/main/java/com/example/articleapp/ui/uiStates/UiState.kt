package com.example.articleapp.ui.uiStates

import com.example.articleapp.domain.models.Article

sealed interface UiState {

    data object Loading: UiState

    data class Success(
        val articles: List<Article>
    ): UiState

    data class Error(
        val message: String
    ): UiState
}