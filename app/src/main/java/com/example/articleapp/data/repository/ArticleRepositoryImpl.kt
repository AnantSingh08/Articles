package com.example.articleapp.data.repository

import com.example.articleapp.data.local.ArticleDao
import com.example.articleapp.data.local.ArticleEntity
import com.example.articleapp.data.remote.datasource.ArticleDataSource
import com.example.articleapp.domain.models.Article
import com.example.articleapp.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepositoryImpl(
    val articleDao: ArticleDao,
    val dataSource: ArticleDataSource
) : ArticleRepository {
    override fun getAllArticles(): Flow<List<Article>> {

        return articleDao.getAllArticles().map { entities ->
            entities.map { entity ->
                Article(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    isBookmarked = entity.isBookmarked,
                    imageUrl = entity.imageUrl
                )
            }
        }
    }

    override suspend fun refreshArticles() {
        val response = dataSource.getArticles()

        val entities = response.articles.map { dto->
            ArticleEntity(
                id = dto.id,
                title = dto.title,
                description = dto.description,
                isBookmarked = false,
                imageUrl = dto.imageUrl
            )
        }

        articleDao.insertArticles(entities)
    }
}