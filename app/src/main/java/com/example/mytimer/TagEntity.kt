package com.example.mytimer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class TagEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: String,
    val time: String)
