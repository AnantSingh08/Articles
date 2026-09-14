package com.example.articleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.articleapp.ui.navigation.ArticleApp
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModel
import com.example.articleapp.ui.screens.articles.viewmodels.ArticleViewModelFactory
import com.example.articleapp.ui.theme.ArticleAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as ArticleApplication

        val factory = ArticleViewModelFactory(
            app.articleRepository
        )

        val viewModel = ViewModelProvider(
            this,
            factory
        )[ArticleViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            ArticleAppTheme {
                ArticleApp(viewModel)
            }
        }
    }
}
