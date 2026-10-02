package com.example.articleapp.data.repository

import com.example.articleapp.data.local.ArticleDao
import com.example.articleapp.data.local.ArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeArticleDao: ArticleDao {

    var articles = emptyList<ArticleEntity>()

    override fun getAllArticles(): Flow<List<ArticleEntity>> {
        return flowOf(articles)
    }

    override suspend fun replaceAllArticles(articles: List<ArticleEntity>) {
        this.articles = articles
    }

    override suspend fun getArticleById(articleId: Long): ArticleEntity? {
        return articles.find { it.id == articleId }
    }

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllArticles() {
        TODO("Not yet implemented")
    }

    override suspend fun updateBookmark(articleId: Long, isBookmarked: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getBookmarkedArticles(): Flow<List<ArticleEntity>> {
        TODO("Not yet implemented")
    }

}