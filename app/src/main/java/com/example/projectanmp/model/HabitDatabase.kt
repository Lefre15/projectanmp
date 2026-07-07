package com.example.projectanmp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Habit::class, User::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: HabitDatabase? = null
        private val LOCK = Any()
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    instance?.userDao()?.insert(User(username = "student", password = "123"))
                }
            }
        }
        fun getDatabase(context: Context): HabitDatabase {
            return instance ?: synchronized(LOCK) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_db"
                ).addCallback(roomCallback).build()
                instance = newInstance
                newInstance
            }
        }
    }
}