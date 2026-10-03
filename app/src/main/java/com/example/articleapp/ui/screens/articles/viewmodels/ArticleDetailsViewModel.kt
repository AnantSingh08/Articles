package com.example.articleapp.ui.screens.articles.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleapp.domain.models.Article
import com.example.articleapp.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    private val repository: ArticleRepository
): ViewModel() {

    private val _article = MutableStateFlow<Article?> (null)
    val article = _article.asStateFlow()

    fun loadArticle(articleId: Long) {
        viewModelScope.launch {
            _article.value = repository.getArticleById(articleId)
        }
    }
}