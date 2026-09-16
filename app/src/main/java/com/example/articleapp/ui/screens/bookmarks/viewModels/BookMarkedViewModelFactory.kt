package com.example.articleapp.ui.screens.bookmarks.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.articleapp.domain.repository.ArticleRepository

class BookMarkedViewModelFactory(
    val repository: ArticleRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkedViewModel::class.java)) {
            return BookmarkedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}