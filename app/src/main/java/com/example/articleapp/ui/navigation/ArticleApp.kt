package com.example.articleapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.articleapp.domain.repository.ArticleRepository
import com.example.articleapp.ui.screens.articles.ArticleDetailsScreen
import com.example.articleapp.ui.screens.articles.ArticlesScreen
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModelFactory
import com.example.articleapp.ui.uiStates.ArticleUiState

@Composable
fun ArticleApp(repository: ArticleRepository) {

    val factory = ArticleViewModelFactory(
        repository
    )

    val viewModel: ArticleViewModel = viewModel(
        factory = factory
    )

    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        ArticleUiState.Loading
    )

    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle(
        false
    )

    ArticleNavHost(
        navController = navController,
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refreshArticles
    )
}

@Composable
fun ArticleNavHost(
    navController: NavHostController,
    uiState: ArticleUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ArticleRoute.Articles
    ) {
        composable<ArticleRoute.Articles> {
            when (uiState) {
                ArticleUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ArticleUiState.Success -> {
                    ArticlesScreen(
                        articles = uiState.articles,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        onArticleClick = { articleId ->
                            navController.navigate(
                                route = ArticleRoute.Details(articleId)
                            )
                        }
                    )
                }

                is ArticleUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(uiState.message)
                    }
                }
            }

        }

        composable<ArticleRoute.Details> { backStackEntry ->

            val route = backStackEntry.toRoute<ArticleRoute.Details>()
            val articleId = route.articleId

            if (uiState is ArticleUiState.Success) {
                val article = uiState.articles.first {
                    it.id == articleId
                }
                ArticleDetailsScreen(
                    article = article,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}