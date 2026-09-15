package com.example.articleapp.ui.screens.articles.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleapp.domain.repository.ArticleRepository
import com.example.articleapp.ui.uiStates.ArticleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ArticleUiState>(
        ArticleUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadArticles()
    }

    private fun observeArticles() {
        viewModelScope.launch {
            repository.getAllArticles().collect { articles ->
                if (articles.isNotEmpty()) {
                    _uiState.value = ArticleUiState.Success(
                        articles
                    )
                }
            }
        }
    }

    fun refreshArticles() {
        println("REFRESH CALLED")
        viewModelScope.launch {

            _isRefreshing.value = true

            try {
                repository.refreshArticles()

                val articles = repository.getAllArticles().first()
                if (articles.isNotEmpty()) {
                    _uiState.value = ArticleUiState.Success(
                        articles
                    )
                }
            } catch (e: Exception) {
                // We'll handle this properly next
                println("REFRESH NETWORK FAILED: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadArticles() {
        viewModelScope.launch {
            try {
                repository.refreshArticles()

                val articles = repository.getAllArticles().first()

                if (articles.isNotEmpty()) {
                    _uiState.value = ArticleUiState.Success(articles)
                }
            } catch (e: Exception) {
                println("LOAD ARTICLES ERROR: ${e.message}")

                val articles = repository.getAllArticles().first()

                if (articles.isNotEmpty()) {
                    _uiState.value = ArticleUiState.Success(articles)
                } else {
                    _uiState.value = ArticleUiState.Error(
                        "Unable to load articles"
                    )
                }
            }
            observeArticles()
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