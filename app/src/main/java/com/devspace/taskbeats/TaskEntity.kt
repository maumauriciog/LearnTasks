package com.devspace.taskbeats

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class TaskEntity(
    @ColumnInfo("Name_category")
    val NameCategory: String,
    @ColumnInfo("name_Task")
    val NameTask: String
)