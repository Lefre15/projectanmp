package com.example.projectanmp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String?,
    var description: String?,
    var goal: Int?,
    var unit: String?,
    var icon: String?,
    var progress: Int?
)