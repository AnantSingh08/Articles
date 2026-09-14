package com.example.articleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.articleapp.ui.navigation.ArticleApp
import com.example.articleapp.ui.theme.ArticleAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val app = application as ArticleApplication
        val repository = app.articleRepository

        setContent {
            ArticleAppTheme {
                ArticleApp(repository)
            }
        }
    }
}
