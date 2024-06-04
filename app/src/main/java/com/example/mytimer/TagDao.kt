package com.example.mytimer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TagDao {
    @Insert
    fun insert(tag: TagEntity)

    @Update
    fun update(tag: TagEntity)

    @Delete
    fun delete(tag: TagEntity)

    // Add query to get specific tag
    @Query("SELECT * FROM tag_table WHERE name = :name")
    fun getTag(name: String): TagEntity

    // Add query to get all tags on certain day
    @Query("SELECT * FROM tag_table WHERE date = :date")
    fun getTagsToday(date: String): List<TagEntity>

    //Add query to get tags from range of dates
    @Query("SELECT * FROM tag_table WHERE date BETWEEN :startDate AND :endDate")
    fun getTagsRange(startDate: String, endDate: String): List<TagEntity>
}