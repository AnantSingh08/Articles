package com.example.articleapp.ui.screens.articles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.articleapp.R
import com.example.articleapp.domain.models.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    articles: List<Article>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onArticleClick: (Long) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Articles",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                items(
                    items = articles,
                    key = { article -> article.id }
                ) { article ->
                    ArticleItem(article = article, onArticleClick = onArticleClick, onBookmarkClick = onBookmarkClick)
                }
            }
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onArticleClick: (Long) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        onClick = {
            onArticleClick(article.id)
        }
    ) {
        Row() {
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = article.imageUrl,
                contentDescription = article.title,
                onError = {
                    println("IMAGE ERROR: ${it.result.throwable}")
                },
                placeholder = painterResource(R.drawable.outline_ad_group_24),
                error = painterResource(R.drawable.outline_ad_group_24)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = article.title,
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = article.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if(article.isBookmarked) {
                IconButton(
                    onClick = {
                        onBookmarkClick (
                            article.id,
                            false
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.BookmarkAdded,
                        contentDescription = "Bookmark",
                    )
                }

            } else {
                IconButton(
                    onClick = {
                        onBookmarkClick (
                            article.id,
                            true
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.BookmarkBorder,
                        contentDescription = "Bookmark"
                    )
                }
            }
        }
    }
}