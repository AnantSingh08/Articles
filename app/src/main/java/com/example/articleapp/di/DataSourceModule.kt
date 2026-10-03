package com.example.articleapp.di

import com.example.articleapp.data.remote.datasource.ArticleDataSource
import com.example.articleapp.data.remote.datasource.ArticleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindArticleDataSource(
        dataSource: ArticleDataSourceImpl
    ): ArticleDataSource
}