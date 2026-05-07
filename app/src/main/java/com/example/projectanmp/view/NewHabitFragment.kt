package com.example.projectanmp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectanmp.databinding.FragmentNewHabitBinding
import com.example.projectanmp.viewmodel.HabitViewModel

class NewHabitFragment : Fragment() {

    private var _binding: FragmentNewHabitBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]

        setupSpinner()
        setupButton()
    }

    private fun setupSpinner() {
        val icons = listOf("fitness", "water", "books", "meditate")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            icons
        )

        binding.spIcon.setAdapter(adapter)
    }

    private fun setupButton() {
        binding.btnCreate.setOnClickListener {

            val name = binding.txtName.text.toString()
            val desc = binding.txtDesc.text.toString()
            val goalText = binding.txtGoal.text.toString()
            val unit = binding.txtUnit.text.toString()
            val icon = binding.spIcon.text.toString()

            val success = viewModel.createHabit(name, desc, goalText, unit, icon)

            if (!success) {
                Toast.makeText(context, "Input tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "Habit berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}