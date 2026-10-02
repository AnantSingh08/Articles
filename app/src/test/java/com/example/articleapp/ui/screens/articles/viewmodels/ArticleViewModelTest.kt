package com.example.articleapp.ui.screens.articles.viewmodels

import com.example.articleapp.domain.models.Article
import com.example.articleapp.ui.MainDispatcherRule
import com.example.articleapp.ui.uiStates.UiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeArticleRepository
    private lateinit var viewModel: ArticleViewModel

    @Before
    fun setup() {
        repository = FakeArticleRepository()
    }

    @Test
    fun observeArticles_whenNoArticlesExist_setErrorState() = runTest {
        viewModel = ArticleViewModel(repository)

        assertEquals(
            UiState.Error("No articles to show"),
            viewModel.uiState.value
        )
    }

    @Test
    fun observeArticles_whenArticlesExist_setsSuccessState() = runTest {
        val articles = listOf(
            Article(
                id = 1L,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = false,
                imageUrl = null
            )
        )

        repository.articles = articles

        viewModel = ArticleViewModel(repository)

        assertEquals(
            UiState.Success(articles),
            viewModel.uiState.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshArticles_whenCalled_refreshRepository() = runTest {
        viewModel = ArticleViewModel(repository)

        repository.refreshCalled = false
        repository.refreshArticles()
        advanceUntilIdle()

        assertEquals(true, repository.refreshCalled)
        assertEquals(false, viewModel.isRefreshing.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun refreshArticles_whenRefreshFails_keepsCachedArticles() = runTest {
        val cachedArticles = listOf(
            Article(
                id = 1L,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = true,
                imageUrl = null
            )
        )

        repository.articles = cachedArticles
        repository.shouldFailRefresh = true

        viewModel = ArticleViewModel(repository)

        viewModel.refreshArticles()
        advanceUntilIdle()

        assertEquals(
            UiState.Success(cachedArticles),
            viewModel.uiState.value
        )

        assertEquals(
            false,
            viewModel.isRefreshing.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateBookmark_whenCalled_updatesBookmarkInRepository() = runTest {
        viewModel = ArticleViewModel(repository)

        viewModel.updateBookMark(
            articleId = 1L,
            isBookmarked = true
        )

        advanceUntilIdle()

        assertEquals(
            1L,
            repository.updatedArticleId
        )

        assertEquals(
            true,
            repository.updatedBookmarkState
        )
    }

    @Test
    fun updateSearchQuery_whenQueryMatchesArticles_returnsFilteredArticles() = runTest {
        val articles = listOf(
            Article(
                id = 1L,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = false,
                imageUrl = null
            ),
            Article(
                id = 2L,
                title = "Android Architecture",
                description = "Understanding Android architecture",
                isBookmarked = false,
                imageUrl = null
            ),
            Article(
                id = 3L,
                title = "Kotlin Flow",
                description = "Reactive streams with Kotlin",
                isBookmarked = false,
                imageUrl = null
            )
        )

        repository.articles = articles
        viewModel = ArticleViewModel(repository)

        viewModel.updateSearchQuery("Kotlin")

        val filteredArticles = viewModel.filteredArticles.first()

        assertEquals(
            listOf(articles[0], articles[2]),
            filteredArticles
        )
    }

}