package com.twentiecker.storyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.twentiecker.storyapp.model.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)

abstract class ListStoryDatabase : RoomDatabase() {
    // G PAKE DAO KAYAK DI NEWS APP ???

    companion object {
        @Volatile
        private var INSTANCE: ListStoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ListStoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ListStoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}