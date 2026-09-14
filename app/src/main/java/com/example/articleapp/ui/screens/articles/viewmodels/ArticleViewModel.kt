package com.example.articleapp.ui.screens.articles.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.articleapp.domain.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository
): ViewModel() {

    val articles = repository.getAllArticles()

    fun refreshArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshArticles()
        }
    }

}