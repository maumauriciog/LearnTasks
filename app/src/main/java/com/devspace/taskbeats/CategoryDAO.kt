package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDAO{
    @Query("Select * from CategoryEntity")
    fun getAll(): List<CategoryEntity>

    @Insert
    fun insertAll(categoryEntity: List<CategoryEntity>)
}