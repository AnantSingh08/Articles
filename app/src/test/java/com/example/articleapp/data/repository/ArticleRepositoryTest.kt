package com.example.articleapp.data.repository

import com.example.articleapp.data.local.ArticleEntity
import com.example.articleapp.data.remote.dto.ArticleDto
import com.example.articleapp.data.remote.dto.ArticleResponseDto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ArticleRepositoryTest {
    private lateinit var fakeArticleDao: FakeArticleDao

    @Before
    fun setup() {
        fakeArticleDao = FakeArticleDao()
    }

    @Test
    fun refreshArticles_whenLocalDatabaseIsEmpty_storesMappedArticles() = runTest {
        //Arrange
        val testResponse = ArticleResponseDto(
            articles = listOf(
                ArticleDto(
                    id = 1L,
                    title = "Kotlin Coroutines",
                    description = "Understanding Coroutines",
                    imageUrl = null,
                    isBookmarked = false,
                ),
                ArticleDto(
                    id = 2L,
                    title = "Jetpack Compose",
                    description = "Building UI with Compose",
                    imageUrl = "https://example.com/compose.png",
                    isBookmarked = true,
                ),
            ),
        )

        val fakeDataSource = FakeArticleDataSource(testResponse)

        val repository = ArticleRepositoryImpl(
            articleDao = fakeArticleDao,
            dataSource = fakeDataSource
        )

        //Act
        repository.refreshArticles()

        //Assert
        val result = fakeArticleDao.articles

        val expected = listOf(
            ArticleEntity(
                id = 1L,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = false,
                imageUrl = null,
                category = "",
                publishedAt = 0L
            ),
            ArticleEntity(
                id = 2L,
                title = "Jetpack Compose",
                description = "Building UI with Compose",
                imageUrl = "https://example.com/compose.png",
                isBookmarked = false,
                category = "",
                publishedAt = 0L
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun refreshArticles_whenExistingBookmarkExists_preservesBookmarkState() = runTest {
        //Arrange
        val existingArticle = ArticleEntity(
            id = 1L,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null,
            category = "",
            publishedAt = 0L
        )

        fakeArticleDao.articles = listOf(existingArticle)

        val testResponse = ArticleResponseDto(
            articles = listOf(
                ArticleDto(
                    id = 1L,
                    title = "Kotlin Coroutines",
                    description = "Understanding Coroutines",
                    imageUrl = null,
                    isBookmarked = false,
                ),
            ),
        )
        val fakeDataSource = FakeArticleDataSource(testResponse)

        val repository = ArticleRepositoryImpl(
            articleDao = fakeArticleDao,
            dataSource = fakeDataSource
        )

        //Act
        repository.refreshArticles()

        //Assert
        val result = fakeArticleDao.articles
        assertEquals(
            true,
            result.first().isBookmarked
        )
    }

    @Test
    fun refreshArticlesWithNewArticles_existingBookmarksPreserved_newArticlesUnbookmarked() = runTest{
        //Arrange
        val existingArticles = listOf<ArticleEntity>(
            ArticleEntity(
                id = 1,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = true,
                imageUrl = null,
                category = "",
                publishedAt = 0L
            ),
            ArticleEntity(
                id = 2L,
                title = "Jetpack Compose",
                description = "Building UI with Compose",
                imageUrl = "https://example.com/compose.png",
                isBookmarked = true,
                category = "",
                publishedAt = 0L
            )
        )

        fakeArticleDao.articles = existingArticles
        val testResponse = ArticleResponseDto(
            articles = listOf(
                ArticleDto(
                    id = 1L,
                    title = "Kotlin Coroutines",
                    description = "Understanding Coroutines",
                    imageUrl = null,
                ),
                ArticleDto(
                    id = 2L,
                    title = "Jetpack Compose",
                    description = "Building UI with Compose",
                    imageUrl = "https://example.com/compose.png",
                ),
                ArticleDto(
                    id = 3L,
                    title = "Kotlin Coroutines",
                    description = "Understanding Coroutines",
                    imageUrl = null,
                ),
                ArticleDto(
                    id = 4L,
                    title = "Jetpack Compose",
                    description = "Building UI with Compose",
                    imageUrl = "https://example.com/compose.png",
                ),
            ),
        )

        val fakeDataSource = FakeArticleDataSource(testResponse)

        val repository = ArticleRepositoryImpl(
            articleDao = fakeArticleDao,
            dataSource = fakeDataSource
        )

        //Act
        repository.refreshArticles()

        //Assert
        assertEquals(true, fakeArticleDao.getArticleById(1L)?.isBookmarked)
        assertEquals(true, fakeArticleDao.getArticleById(2L)?.isBookmarked)
        assertEquals(4, fakeArticleDao.articles.size)
        assertEquals(false, fakeArticleDao.getArticleById(3L)?.isBookmarked)
        assertEquals(false, fakeArticleDao.getArticleById(4L)?.isBookmarked)
    }
}