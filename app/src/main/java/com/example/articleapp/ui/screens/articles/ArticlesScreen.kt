package com.example.articleapp.ui.screens.articles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.articleapp.domain.models.Article

@Composable
fun ArticlesScreen(
    articles: List<Article>,
    onArticleClick: (Long) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            items(
                items = articles,
                key = { article -> article.id }
            ) { article ->
                ArticleItem(article = article, onArticleClick = onArticleClick)
            }
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onArticleClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onArticleClick(article.id)
        }
    ) {
        Row() {
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = article.imageUrl,
                contentDescription = article.title
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = article.title
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = article.description,
                    maxLines = 1
                )
            }
        }
    }
}