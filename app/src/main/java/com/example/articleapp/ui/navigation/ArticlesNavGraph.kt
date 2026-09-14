package com.example.articleapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.articleapp.domain.models.Article
import com.example.articleapp.ui.screens.articles.ArticleDetailsScreen
import com.example.articleapp.ui.screens.articles.ArticlesScreen
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel

@Composable
fun ArticleApp(viewModel: ArticleViewModel) {
    val navController = rememberNavController()
    val articles = viewModel.articles.collectAsStateWithLifecycle(
        listOf()
    ).value
    ArticleNavHost(navController = navController, articles)
}

@Composable
fun ArticleNavHost(
    navController: NavHostController,
    articles: List<Article>
) {
    NavHost(
        navController = navController,
        startDestination = ArticleRoute.Articles
    ) {
        composable<ArticleRoute.Articles> {
            ArticlesScreen(
                articles = articles,
                onArticleClick = { articleId ->
                    navController.navigate(
                        route = ArticleRoute.Details(articleId)
                    )
                }
            )
        }

        composable<ArticleRoute.Details> {backStackEntry ->

            val route = backStackEntry.toRoute<ArticleRoute.Details>()
            val articleId = route.articleId
            val article = articles.first{
                it.id == articleId
            }
            ArticleDetailsScreen(
                article = article,
                onBackClick = {
                    navController.navigate(
                        route = ArticleRoute.Articles
                    )
                }
            )
        }
    }
}