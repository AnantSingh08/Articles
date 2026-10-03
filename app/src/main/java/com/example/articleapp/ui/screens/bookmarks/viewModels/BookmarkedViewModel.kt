package com.example.articleapp.ui.screens.bookmarks.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleapp.domain.repository.ArticleRepository
import com.example.articleapp.ui.uiStates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedViewModel @Inject constructor(
    private val repository: ArticleRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadBookmarkedArticles()
    }

    private fun loadBookmarkedArticles() {
        viewModelScope.launch {
            try {
                repository.getBookmarkedArticles().collect { articles ->
                    if(articles.isNotEmpty()) {
                        _uiState.value = UiState.Success(
                            articles
                        )
                    }else {
                        _uiState.value = UiState.Error(
                            message = "No Bookmarked Articles"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = "Unable to load Bookmarked Articles"
                )
            }
        }
    }
}
