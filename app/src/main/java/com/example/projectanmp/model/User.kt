package com.example.projectanmp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String,
    var password: String,
    var isLoggedIn:Boolean=false
)