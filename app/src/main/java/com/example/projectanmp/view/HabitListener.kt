package com.example.projectanmp.view

import com.example.projectanmp.model.Habit

interface HabitListener {
    fun onIncrement(habit: Habit)
    fun onDecrement(habit: Habit)
}