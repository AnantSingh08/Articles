package com.example.articleapp.ui.screens.articles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.articleapp.domain.models.Article

@Composable
fun ArticleDetailsScreen(article: Article) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    modifier = Modifier.size(80.dp),
                    model = article.imageUrl,
                    contentDescription = article.title
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}