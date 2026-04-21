package com.example.projectanmp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectanmp.model.Habit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _habitList = MutableLiveData<MutableList<Habit>>()
    val habitList: LiveData<MutableList<Habit>> get() = _habitList

    init {
        loadHabits()
    }

    private fun loadHabits() {
        val json = prefs.getString("habits", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Habit>>() {}.type
            _habitList.value = gson.fromJson(json, type)
        } else {
            _habitList.value = mutableListOf()
        }
    }

    private fun saveHabits() {
        val json = gson.toJson(_habitList.value)
        prefs.edit().putString("habits", json).apply()
    }

    fun addHabit(habit: Habit) {
        val currentList = _habitList.value ?: mutableListOf()
        currentList.add(habit)
        _habitList.value = currentList
        saveHabits()
    }

    fun incrementProgress(habitId: String) {
        val currentList = _habitList.value ?: return
        val habit = currentList.find { it.id == habitId } ?: return
        val goal = habit.goal ?: return
        if ((habit.progress ?: 0) < goal) {
            habit.progress = (habit.progress ?: 0) + 1
            _habitList.value = currentList
            saveHabits()
        }
    }

    fun decrementProgress(habitId: String) {
        val currentList = _habitList.value ?: return
        val habit = currentList.find { it.id == habitId } ?: return
        if ((habit.progress ?: 0) > 0) {
            habit.progress = (habit.progress ?: 0) - 1
            _habitList.value = currentList
            saveHabits()
        }
    }
}