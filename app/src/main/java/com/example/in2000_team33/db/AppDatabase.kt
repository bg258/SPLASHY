package com.example.in2000_team33.db

import android.content.Context
import androidx.room.*

@Database(entities = [BadestedEntity::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun badestedDao(): BadestedDao

    companion object {
        private var db: AppDatabase? = null

        fun hentDatabase(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(context, AppDatabase::class.java, "badested-db")
                    .createFromAsset("database.db")
                    .build().also { db = it }
        }
    }
}
