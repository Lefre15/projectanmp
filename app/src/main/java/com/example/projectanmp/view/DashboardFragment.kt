package com.example.projectanmp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectanmp.R
import com.example.projectanmp.databinding.FragmentDashboardBinding
import com.example.projectanmp.viewmodel.HabitViewModel

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: HabitViewModel
    private lateinit var adapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HabitViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.actionNewHabitFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = HabitAdapter(
            context = requireContext(),
            habitList = mutableListOf(),
            onIncrement = { habitId -> viewModel.incrementProgress(habitId) },
            onDecrement = { habitId -> viewModel.decrementProgress(habitId) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.habitList.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)

            // Tampilkan empty state jika list kosong
            if (list.isEmpty()) {
                binding.txtEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.txtEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }
}