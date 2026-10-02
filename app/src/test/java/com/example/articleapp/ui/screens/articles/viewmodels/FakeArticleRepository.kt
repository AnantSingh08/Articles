package com.example.articleapp.ui.screens.articles.viewmodels

import com.example.articleapp.domain.models.Article
import com.example.articleapp.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeArticleRepository : ArticleRepository {
    var articles = emptyList<Article>()

    var refreshCalled = false
    var shouldFailRefresh = false
    var updatedArticleId: Long? = null
    var updatedBookmarkState: Boolean? = null

    override fun getAllArticles(): Flow<List<Article>> {
        return flowOf(articles)
    }

    override suspend fun getArticleById(articleId: Long): Article? {
        TODO("Not yet implemented")
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshArticles() {
        refreshCalled = true

        if (shouldFailRefresh) {
            throw Exception("Network error")
        }
    }

    override suspend fun updateBookmark(articleId: Long, isBookmarked: Boolean) {
        updatedArticleId = articleId
        updatedBookmarkState = isBookmarked
    }
}