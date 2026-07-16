package com.example.projectanmp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectanmp.databinding.FragmentEditHabitBinding
import com.example.projectanmp.viewmodel.HabitViewModel

class EditHabitFragment : Fragment() {

    private lateinit var binding: FragmentEditHabitBinding
    private lateinit var viewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]

        val args = EditHabitFragmentArgs.fromBundle(requireArguments())
        val habitId = args.habitId

        setupSpinner()

        viewModel.loadHabit(habitId)

        viewModel.selectedHabit.observe(viewLifecycleOwner) { habit ->

            binding.habit = habit

            binding.txtGoal.setText(habit.goal.toString())
            binding.spIcon.setText(habit.icon, false)
        }

        binding.btnSubmit.setOnClickListener {

            val habit = binding.habit ?: return@setOnClickListener

            habit.name = binding.txtName.text.toString()
            habit.description = binding.txtDesc.text.toString()
            habit.goal = binding.txtGoal.text.toString().toIntOrNull()
            habit.unit = binding.txtUnit.text.toString()
            habit.icon = binding.spIcon.text.toString()


            viewModel.updateHabit(habit)

            findNavController().popBackStack()
        }
    }

    private fun setupSpinner() {

        val icons = listOf(
            "fitness",
            "water",
            "books",
            "meditate"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            icons
        )

        binding.spIcon.setAdapter(adapter)
    }
}