package com.example.articleapp

import android.app.Application
import androidx.room3.Room
import com.example.articleapp.data.local.ArticleDao
import com.example.articleapp.data.local.ArticleDatabase
import com.example.articleapp.data.remote.KtorClient
import com.example.articleapp.data.remote.api.ArticleApi
import com.example.articleapp.data.remote.datasource.ArticleDataSource
import com.example.articleapp.data.remote.datasource.KtorArticleDataSource
import com.example.articleapp.data.repository.ArticleRepositoryImpl
import com.example.articleapp.domain.repository.ArticleRepository

class ArticleApplication: Application() {
    lateinit var db: ArticleDatabase
    lateinit var articleDao: ArticleDao
    lateinit var api: ArticleApi
    lateinit var remoteDataSource: ArticleDataSource
    lateinit var articleRepository: ArticleRepository


    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            context = applicationContext,
            klass = ArticleDatabase::class.java,
            name = "ArticleDB",
        ).build()

        articleDao = db.articleDao()
        api = ArticleApi(KtorClient.client)
        remoteDataSource = KtorArticleDataSource(api)
        articleRepository = ArticleRepositoryImpl(
            articleDao,
            remoteDataSource
        )
    }



}