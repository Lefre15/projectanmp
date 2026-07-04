package com.example.projectanmp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDao {
    @Insert
    fun insert(habit: Habit)
    @Query("SELECT * FROM habit")
    fun selectAll(): List<Habit>
    @Query("SELECT * FROM habit WHERE id = :id")
    fun selectById(id: Int): Habit?
    @Update
    fun update(habit: Habit)
}