package com.example.articleapp.ui.screens.articles.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleapp.domain.repository.ArticleRepository
import com.example.articleapp.ui.uiStates.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        observeArticles()
        refreshArticles()
    }

    private fun observeArticles() {
        viewModelScope.launch {
            repository.getAllArticles().collect { articles ->
                _uiState.value = if (articles.isNotEmpty()) {
                    UiState.Success(
                        articles
                    )
                } else {
                    UiState.Error("No articles to show")
                }
            }
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {

            _isRefreshing.value = true

            try {
                repository.refreshArticles()
            } catch (e: Exception) {
                // Keep showing whatever Room currently has.
                // observeArticles() is responsible for UiState.
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun updateBookMark(
        articleId: Long,
        isBookmarked: Boolean
    ) {
        viewModelScope.launch {
            repository.updateBookmark(
                articleId = articleId,
                isBookmarked = isBookmarked,
            )
        }
    }

}