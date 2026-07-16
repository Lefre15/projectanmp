package com.example.projectanmp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User?
    @Update
    fun update(user: User)
    @Query("UPDATE user SET isLoggedIn = 0")
    fun logoutAll()
    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): User?
}