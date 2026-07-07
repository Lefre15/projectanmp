package com.example.projectanmp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectanmp.R
import com.example.projectanmp.databinding.ItemHabitCardBinding
import com.example.projectanmp.model.Habit

class HabitAdapter(
    private var habitList: MutableList<Habit>,
    private val onIncrement: (Int) -> Unit,
    private val onDecrement: (Int) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>(), HabitListener {

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

        // Data Binding — attribute binding dan listener binding
        binding.habit = habit
        binding.listener = this

        // Icon
        val iconRes = when (habit.icon) {
            "fitness"  -> R.drawable.baseline_fitness_24
            "water"    -> R.drawable.baseline_water_24
            "books"    -> R.drawable.baseline_books_24
            "meditate" -> R.drawable.baseline_meditate_24
            else       -> R.drawable.baseline_fitness_24
        }
        binding.ivHabitIcon.setImageResource(iconRes)

        // Progress count
        binding.progressCount.text = "${habit.progress} / ${habit.goal} ${habit.unit}"

        // Status label
        val progress = habit.progress ?: 0
        val goal = habit.goal ?: 1
        if (progress >= goal) {
            binding.habitStatus.text = "Completed"
            binding.habitStatus.setBackgroundColor(
                holder.itemView.context.getColor(android.R.color.holo_green_light)
            )
        } else {
            binding.habitStatus.text = "In Progress"
            binding.habitStatus.setBackgroundColor(
                holder.itemView.context.getColor(android.R.color.holo_orange_light)
            )
        }
    }

    override fun getItemCount(): Int = habitList.size

    // Implementasi HabitListener — diteruskan ke lambda dari DashboardFragment
    override fun onIncrement(habit: Habit) { onIncrement(habit.id) }
    override fun onDecrement(habit: Habit) { onDecrement(habit.id) }

    fun updateList(newList: MutableList<Habit>) {
        habitList = newList
        notifyDataSetChanged()
    }
}