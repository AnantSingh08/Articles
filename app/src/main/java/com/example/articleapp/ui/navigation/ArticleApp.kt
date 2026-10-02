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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.articleapp.domain.models.Article
import com.example.articleapp.domain.repository.ArticleRepository
import com.example.articleapp.ui.components.ArticleBottomBar
import com.example.articleapp.ui.screens.articles.ArticleDetailsScreen
import com.example.articleapp.ui.screens.articles.ArticlesScreen
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModelFactory
import com.example.articleapp.ui.screens.bookmarks.BookmarkScreen
import com.example.articleapp.ui.screens.bookmarks.viewModels.BookMarkedViewModelFactory
import com.example.articleapp.ui.screens.bookmarks.viewModels.BookmarkedViewModel
import com.example.articleapp.ui.uiStates.UiState

@Composable
fun ArticleApp(repository: ArticleRepository) {

    val navController = rememberNavController()

    ArticleNavHost(
        navController = navController,
        repository = repository,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArticleNavHost(
    navController: NavHostController,
    repository: ArticleRepository,
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
                val factory = ArticleViewModelFactory(repository)
                val viewModel: ArticleViewModel = viewModel(factory = factory)
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

                var article by remember {
                    mutableStateOf<Article?>(null)
                }

                LaunchedEffect(articleId) {
                    article = repository.getArticleById(articleId)
                }

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
                val factory = BookMarkedViewModelFactory(repository = repository)
                val viewModel: BookmarkedViewModel = viewModel(
                    factory = factory
                )

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