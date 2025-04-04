package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var Instance: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase {
            return Instance ?: synchronized(this) {
                try {
                    Room.databaseBuilder(
                        context,
                        WordDatabase::class.java,
                        "word_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { Instance = it }
                } catch (e: Exception) {
                    throw RuntimeException("Failed to initialize database", e)
                }
            }
        }
    }
}