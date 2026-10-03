package com.example.articleapp.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.articleapp.ui.components.ArticleBottomBar
import com.example.articleapp.ui.screens.articles.ArticleDetailsScreen
import com.example.articleapp.ui.screens.articles.ArticlesScreen
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleDetailsViewModel
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel
import com.example.articleapp.ui.screens.bookmarks.BookmarkScreen
import com.example.articleapp.ui.screens.bookmarks.viewModels.BookmarkedViewModel
import com.example.articleapp.ui.uiStates.UiState

@Composable
fun ArticleApp() {

    val navController = rememberNavController()

    ArticleNavHost(
        navController = navController,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArticleNavHost(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (navBackStackEntry?.destination?.hasRoute(ArticleRoute.Articles::class) == true ||
                navBackStackEntry?.destination?.hasRoute(ArticleRoute.Bookmarks::class) == true
            ) {
                ArticleBottomBar(navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = ArticleRoute.Articles,
        )
        {
            composable<ArticleRoute.Articles> {
                val viewModel: ArticleViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle(
                    UiState.Loading
                )
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val filteredArticles by viewModel.filteredArticles.collectAsStateWithLifecycle()

                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
                when (val state = uiState) {
                    UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        ArticlesScreen(
                            articles = filteredArticles,
                            isRefreshing = isRefreshing,
                            onRefresh = viewModel::refreshArticles,
                            onArticleClick = { articleId ->
                                navController.navigate(
                                    route = ArticleRoute.Details(articleId)
                                )
                            },
                            onBookmarkClick = viewModel::updateBookMark,
                            searchQuery = searchQuery,
                            onSearchQueryChanged = viewModel::updateSearchQuery
                        )
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(state.message)
                        }
                    }
                }

            }

            composable<ArticleRoute.Details> { backStackEntry ->

                val route = backStackEntry.toRoute<ArticleRoute.Details>()
                val articleId = route.articleId

                val viewModel: ArticleDetailsViewModel = hiltViewModel()


                LaunchedEffect(articleId) {
                    viewModel.loadArticle(articleId)
                }

                val article by viewModel.article.collectAsStateWithLifecycle()

                article?.let {
                    ArticleDetailsScreen(
                        article = it,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

            }

            composable<ArticleRoute.Bookmarks> {
                val viewModel: BookmarkedViewModel = hiltViewModel()

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                when (val state = uiState) {
                    UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        BookmarkScreen(
                            articles = state.articles,
                            onArticleClick = { articleId ->
                                navController.navigate(
                                    ArticleRoute.Details(
                                        articleId
                                    )
                                )
                            }
                        )
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(state.message)
                        }
                    }

                }

            }
        }
    }

}