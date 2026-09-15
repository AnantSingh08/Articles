package com.example.articleapp.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()

    @Transaction
    suspend fun replaceAllArticles(articles: List<ArticleEntity>) {
        deleteAllArticles()
        insertArticles(articles)
    }

    @Query("""
        UPDATE articles
        SET isBookmarked = :isBookmarked
        WHERE id = :articleId
    """)
    suspend fun updateBookmark(
        articleId: Long,
        isBookmarked: Boolean
    )
}
