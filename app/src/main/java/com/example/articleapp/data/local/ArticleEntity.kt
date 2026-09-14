package com.example.articleapp.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val isBookmarked: Boolean,
    val imageUrl: String?,
)
