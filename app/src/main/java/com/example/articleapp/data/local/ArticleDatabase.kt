package com.example.articleapp.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [ArticleEntity::class],
    version = 2
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}