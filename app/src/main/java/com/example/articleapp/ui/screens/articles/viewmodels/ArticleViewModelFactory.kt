package com.example.articleapp.ui.screens.articles.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.articleapp.domain.repository.ArticleRepository

class ArticleViewModelFactory(
    private val repository: ArticleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}