package com.example.articleapp.di

import com.example.articleapp.data.repository.ArticleRepositoryImpl
import com.example.articleapp.domain.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        repository: ArticleRepositoryImpl
    ): ArticleRepository
}