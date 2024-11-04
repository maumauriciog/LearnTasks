package com.devspace.taskbeats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("name_category")
    val nameCategory: String,
    @ColumnInfo("name_Task")
    val nameTask: String
)