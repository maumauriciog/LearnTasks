package com.devspace.taskbeats

import androidx.room.Database

@Database([CategoryEntity::class], version = 1)
abstract class TaskBeatsDataBase{

    abstract fun getCategoryDao(): CategoryDAO
}