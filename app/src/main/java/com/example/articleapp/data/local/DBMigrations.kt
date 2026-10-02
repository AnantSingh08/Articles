package com.example.articleapp.data.local

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1,2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
                ALTER TABLE articles
                ADD COLUMN category TEXT NOT NULL DEFAULT ''
            """.trimIndent()
        )
        connection.execSQL(
            """
                ALTER TABLE articles
                ADD COLUMN publishedAt INTEGER NOT NULL DEFAULT 0
            """.trimIndent()
        )
        connection.execSQL(
            """
            CREATE INDEX index_articles_category
            ON articles(category)
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE INDEX index_articles_publishedAt
            ON articles(publishedAt)
            """.trimIndent()
        )
    }
}