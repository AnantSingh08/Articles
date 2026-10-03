package com.example.articleapp.di

import android.content.Context
import androidx.room3.Room
import com.example.articleapp.data.local.ArticleDao
import com.example.articleapp.data.local.ArticleDatabase
import com.example.articleapp.data.local.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(
        @ApplicationContext context: Context
    ): ArticleDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ArticleDatabase::class.java,
            name = "ArticleDB"
        ).addMigrations(MIGRATION_1_2)
            .build()

    }

    @Provides
    fun provideArticleDao(
        database: ArticleDatabase
    ): ArticleDao {
       return database.articleDao()
    }
}