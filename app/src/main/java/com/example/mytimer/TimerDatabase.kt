package com.example.mytimer

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [TagEntity::class], version = 1, exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}