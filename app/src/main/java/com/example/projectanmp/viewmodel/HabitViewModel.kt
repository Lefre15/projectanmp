package com.example.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectanmp.model.Habit
import com.example.projectanmp.model.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HabitViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val db = HabitDatabase.getDatabase(application)

    private val _habitList = MutableLiveData<MutableList<Habit>>()
    val habitList: LiveData<MutableList<Habit>> get() = _habitList

    private val _selectedHabit = MutableLiveData<Habit>()
    val selectedHabit: LiveData<Habit>
        get() = _selectedHabit

    init {
        loadHabits()
    }

    fun loadHabits() {
        launch {
            val list = db.habitDao().selectAll().toMutableList()
            _habitList.postValue(list)
        }
    }

    fun loadHabit(id: Int) {
        launch {
            val habit = db.habitDao().selectById(id)
            habit?.let {
                _selectedHabit.postValue(it)
            }
        }
    }

    fun incrementProgress(habitId: Int) {
        launch {
            val habit = db.habitDao().selectById(habitId) ?: return@launch
            val goal = habit.goal ?: return@launch
            if ((habit.progress ?: 0) < goal) {
                habit.progress = (habit.progress ?: 0) + 1
                db.habitDao().update(habit)
                loadHabits()
            }
        }
    }

    fun decrementProgress(habitId: Int) {
        launch {
            val habit = db.habitDao().selectById(habitId) ?: return@launch
            if ((habit.progress ?: 0) > 0) {
                habit.progress = (habit.progress ?: 0) - 1
                db.habitDao().update(habit)
                loadHabits()
            }
        }
    }

    fun createHabit(name: String, desc: String, goalText: String, unit: String, icon: String): Boolean {
        if (name.isEmpty() || desc.isEmpty() || goalText.isEmpty() || unit.isEmpty() || icon.isEmpty()) {
            return false
        }
        val goal = goalText.toIntOrNull() ?: return false
        val habit = Habit(name = name, description = desc, goal = goal, unit = unit, icon = icon, progress = 0)
        launch {
            db.habitDao().insert(habit)
            loadHabits()
        }
        return true
    }

    fun updateHabit(habit: Habit) {
        launch {
            db.habitDao().update(habit)
            loadHabits()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}