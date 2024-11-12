package com.devspace.taskbeats

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([CategoryEntity::class, TaskEntity::class], version = 1)
abstract class TaskBeatsDataBase :RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDAO
    abstract fun getTaskDao(): TaskDAO
}