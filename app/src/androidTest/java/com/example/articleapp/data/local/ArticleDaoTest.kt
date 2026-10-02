package com.example.articleapp.data.local

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            ArticleDatabase::class.java
        ).build()

        dao = db.articleDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertArticles_getAllArticles_returnsInsertedArticles() = runTest {

        //Arrange
        val article = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        //Act
        dao.insertArticles(listOf(article))

        //Assert
        val articles = dao.getAllArticles().first()

        assertEquals(1, articles.size)

        assertEquals(article, articles.first())
    }

    //getArticleById()

    @Test
    fun insertArticle_getArticleById_returnsInsertedArticleB() = runTest {
        //Arrange
        val article = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        //Act
        dao.insertArticles(listOf(article))

        //Assert
        val result = dao.getArticleById(articleId = article.id)
        assertEquals(article, result)
    }

    @Test
    fun updateBookMark_existingArticle_returnsUpdatedBookMarkState() = runTest {
        //Arrange
        val article = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        dao.insertArticles(listOf(article))

        //Act
        dao.updateBookmark(articleId = article.id, isBookmarked = !article.isBookmarked)

        //Assert
        val articleResult = dao.getArticleById(articleId = article.id)

        val bookMarkStateOfResult = articleResult?.isBookmarked

        assertEquals(
            !article.isBookmarked,
            articleResult?.isBookmarked
        )
    }

    //testing getBookmarkedArticles()
    @Test
    fun getBookMarkedArticles_returnsOnlyBookMarkedArticles() = runTest {
        //Arrange
        val article1 = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        val article2 = ArticleEntity(
            id = 2,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )
        dao.insertArticles(listOf(article1,article2))

        //Act
        val result = dao.getBookmarkedArticles().first()

        //Assert
        assertEquals(result.size, 1)
        assertEquals(result.first(), article2)
    }

    @Test
    fun getArticleById_unknownId_returnsNull() = runTest {
        //Arrange
        //not inserting any article

        //Act
        val result = dao.getArticleById(999)

        //Assert
        assertNull(result)
    }

    @Test
    fun deleteAllArticles_deletesAllArticles() = runTest {
        //Arrange
        val article1 = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        val article2 = ArticleEntity(
            id = 2,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )
        dao.insertArticles(listOf(article1,article2))

        //Act
        dao.deleteAllArticles()

        //Assert
        assertEquals(0, dao.getAllArticles().first().size)
    }

    @Test
    fun replaceAllArticles_existingArticles_returnTheNewArticles() = runTest {
        //Assert
        val article1 = ArticleEntity(
            id = 1,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        val article2 = ArticleEntity(
            id = 2,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        val article3 = ArticleEntity(
            id = 3,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )
        dao.insertArticles(listOf(article1,article2,article3))
        val initialSize = dao.getAllArticles().first().size

        //Act
        val article5 = ArticleEntity(
            id = 5,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )

        val article4 = ArticleEntity(
            id = 4,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "Android",
            publishedAt = 123L
        )
        dao.replaceAllArticles(listOf(article4, article5))
        //Assert
        val newSize = dao.getAllArticles().first().size
        assertEquals(2, newSize)
        assertEquals(listOf(article4,article5),dao.getAllArticles().first())
    }
}