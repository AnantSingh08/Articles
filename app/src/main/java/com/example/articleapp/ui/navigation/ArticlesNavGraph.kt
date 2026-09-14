package com.example.articleapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.articleapp.ArticleApplication
import com.example.articleapp.domain.models.Article
import com.example.articleapp.ui.screens.articles.ArticleDetailsScreen
import com.example.articleapp.ui.screens.articles.ArticlesScreen
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModelFactory

@Composable
fun ArticleApp() {
    val context = LocalContext.current

    val app = context.applicationContext as ArticleApplication

    val factory = ArticleViewModelFactory(
        app.articleRepository
    )

    val viewModel: ArticleViewModel = viewModel(
        factory = factory
    )

    val navController = rememberNavController()
    val articles = viewModel.articles.collectAsStateWithLifecycle(
        emptyList()
    ).value

    ArticleNavHost(navController = navController, articles = articles)
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

        composable<ArticleRoute.Details> { backStackEntry ->

            val route = backStackEntry.toRoute<ArticleRoute.Details>()
            val articleId = route.articleId
            val article = articles.first {
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