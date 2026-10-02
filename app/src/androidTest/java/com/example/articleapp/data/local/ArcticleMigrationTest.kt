package com.example.articleapp.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.executeSQL
import androidx.room3.useReaderConnection
import androidx.room3.useWriterConnection
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Entity(tableName = "articles")
data class ArticleEntityV1(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val isBookmarked: Boolean,
    val imageUrl: String?
)

@Database(
    entities = [ArticleEntityV1::class],
    version = 1
)
abstract class ArticleDatabaseV1 : RoomDatabase()

@RunWith(AndroidJUnit4::class)
class ArticleMigrationTest {
    private var db: ArticleDatabase? = null

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        db?.close()
    }

    @Test
    fun migrate1to2_preservesExistingData() = runTest {
        val dbName = "migration-test-db"

        val v1Db = Room.databaseBuilder(
            context = context,
            klass = ArticleDatabaseV1::class.java,
            name = dbName
        ).build()

        v1Db.useWriterConnection { connection ->
            connection.executeSQL(
                """
                    INSERT INTO articles (
                        id,
                        title,
                        description,
                        isBookmarked,
                        imageUrl
                    ) VALUES (
                        1,
                        'Understanding Coroutines',
                        'A guide to Kotlin coroutines',
                        1,
                        'https://example.com/coroutines.png'
                    )
                """.trimIndent()
            )
        }

        v1Db.close()

        db = Room.databaseBuilder(
            context = context,
            klass = ArticleDatabase::class.java,
            name = dbName
        ).addMigrations(MIGRATION_1_2).build()

        db?.useReaderConnection { connection ->
            connection.usePrepared(
                "SELECT * FROM articles WHERE id = ?"
            ) { statement ->
                statement.bindLong(1, 1L)

                assertTrue(statement.step())

                assertEquals(1L, statement.getLong(0))
                assertEquals("Understanding Coroutines", statement.getText(1))
                assertEquals("A guide to Kotlin coroutines", statement.getText(2))
                assertEquals(1L, statement.getLong(3))
                assertEquals(
                    "https://example.com/coroutines.png",
                    statement.getText(4)
                )
                assertEquals("", statement.getText(5))
                assertEquals(0L, statement.getLong(6))
            }
        }
    }

}