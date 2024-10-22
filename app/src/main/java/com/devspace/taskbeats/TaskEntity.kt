package com.devspace.taskbeats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo("Name_category")
    val NameCategory: String,
    @ColumnInfo("name_Task")
    val NameTask: String
)