package com.example.articleapp.data.repository

import com.example.articleapp.data.local.ArticleDao
import com.example.articleapp.data.remote.datasource.ArticleDataSource
import com.example.articleapp.data.toDomain
import com.example.articleapp.data.toEntity
import com.example.articleapp.domain.models.Article
import com.example.articleapp.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    private val articleDao: ArticleDao,
    private val dataSource: ArticleDataSource
) : ArticleRepository {
    override fun getAllArticles(): Flow<List<Article>> {

        return articleDao.getAllArticles().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun refreshArticles() {
        val response = dataSource.getArticles()

        val existingArticles = articleDao.getAllArticles().first()

        val existingBookmarks = existingArticles.associate {
            it.id to it.isBookmarked
        }

        val entities = response.articles.map { dto->
            dto.toEntity().copy(
                isBookmarked = existingBookmarks[dto.id] ?: false
            )
        }

        articleDao.replaceAllArticles(entities)
    }

    override suspend fun updateBookmark(articleId: Long, isBookmarked: Boolean) {
        articleDao.updateBookmark(
            articleId = articleId,
            isBookmarked = isBookmarked
        )
    }
}