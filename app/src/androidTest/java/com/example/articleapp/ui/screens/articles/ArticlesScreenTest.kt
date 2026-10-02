package com.example.articleapp.ui.screens.articles

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.articleapp.domain.models.Article
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun articlesScreen_displaysArticles() {
        val articles = listOf(
            Article(
                id = 1L,
                title = "Kotlin Coroutines",
                description = "Understanding Coroutines",
                isBookmarked = false,
                imageUrl = null
            )
        )

        composeTestRule.setContent {
            ArticlesScreen(
                articles = articles,
                isRefreshing = false,
                onRefresh = {},
                onArticleClick = {},
                onBookmarkClick = {_, _ ->}
            )
        }

        composeTestRule
            .onNodeWithText("Kotlin Coroutines")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Understanding Coroutines")
            .assertIsDisplayed()
    }

    @Test
    fun articleItem_whenBookmarkClicked_invokesCallbackWithUpdatedState() {
        val article = Article(
            id = 1L,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null
        )

        var clickedArticleId: Long? = null
        var clickedBookmarkState: Boolean? = null

        composeTestRule.setContent {
            ArticleItem(
                article = article,
                onArticleClick = {},
                onBookmarkClick = { id, isBookmarked ->
                    clickedArticleId = id
                    clickedBookmarkState = isBookmarked
                },
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Bookmark")
            .performClick()

        assertEquals(1L, clickedArticleId)
        assertEquals(true, clickedBookmarkState)
    }

    @Test
    fun articleItem_whenClicked_invokesCallbackWithArticleId() {
        val article = Article(
            id = 1L,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = false,
            imageUrl = null
        )

        var clickedArticleId: Long? = null

        composeTestRule.setContent {
            ArticleItem(
                article = article,
                onArticleClick = {id ->
                    clickedArticleId = id
                },
                onBookmarkClick = {_,_->},
            )
        }

        composeTestRule
            .onNodeWithText("Kotlin Coroutines")
            .performClick()

        assertEquals(1L, clickedArticleId)
    }

    @Test
    fun articleItem_whenBookmarked_displaysBookmarkAddedIcon() {
        val article = Article(
            id = 1L,
            title = "Kotlin Coroutines",
            description = "Understanding Coroutines",
            isBookmarked = true,
            imageUrl = null
        )

        composeTestRule.setContent {
            ArticleItem(
                article = article,
                onArticleClick = {},
                onBookmarkClick = {_,_->}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Bookmark")
            .assertIsDisplayed()
    }
}