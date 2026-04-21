package com.example.projectanmp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectanmp.R
import com.example.projectanmp.databinding.ItemHabitCardBinding
import com.example.projectanmp.model.Habit

class HabitAdapter(
    private val context: Context,
    private var habitList: MutableList<Habit>,
    private val onIncrement: (String) -> Unit,
    private val onDecrement: (String) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemHabitCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        val binding = holder.binding

        // Set teks
        binding.habitName.text = habit.name
        binding.habitDescription.text = habit.description
        binding.progressCount.text = "${habit.progress} / ${habit.goal} ${habit.unit}"

        // Set icon berdasarkan string icon
        val iconRes = when (habit.icon) {
            "fitness"  -> R.drawable.baseline_fitness_24
            "water"    -> R.drawable.baseline_water_24
            "books"    -> R.drawable.baseline_books_24
            "meditate" -> R.drawable.baseline_meditate_24
            else       -> R.drawable.baseline_fitness_24
        }
        binding.ivHabitIcon.setImageResource(iconRes)

        // Set progress bar
        val progress = habit.progress ?: 0
        val goal = habit.goal ?: 1
        binding.progressBar.max = goal
        binding.progressBar.progress = progress

        // Set status label
        if (progress >= goal) {
            binding.habitStatus.text = "Completed"
            binding.habitStatus.setBackgroundColor(
                context.getColor(android.R.color.holo_green_light)
            )
        } else {
            binding.habitStatus.text = "In Progress"
            binding.habitStatus.setBackgroundColor(
                context.getColor(android.R.color.holo_orange_light)
            )
        }

        // Tombol + dan -
        binding.btnIncrement.setOnClickListener { onIncrement(habit.id ?: "") }
        binding.btnDecrement.setOnClickListener { onDecrement(habit.id ?: "") }
    }

    override fun getItemCount(): Int = habitList.size

    fun updateList(newList: MutableList<Habit>) {
        habitList = newList
        notifyDataSetChanged()
    }
}