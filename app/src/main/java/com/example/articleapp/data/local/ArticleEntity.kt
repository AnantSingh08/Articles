package com.example.articleapp.data.local

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "articles",
    indices = [
        Index(value = ["category"]),
        Index(value = ["publishedAt"])
    ]
)
data class ArticleEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val isBookmarked: Boolean,
    val imageUrl: String?,
    val category: String,
    val publishedAt: Long,
)
