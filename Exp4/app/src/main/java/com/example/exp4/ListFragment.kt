package com.example.exp4

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel: DepartmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slidingPaneLayout = requireActivity().findViewById<SlidingPaneLayout>(R.id.slidingPaneLayout)

        val departments = listOf(
            Department(
                "Computer Science & IT",
                "Focuses on software development, artificial intelligence, and networking. Prepares students for careers in the tech industry.",
                ContextCompat.getColor(requireContext(), R.color.olive_green),
            ),
            Department(
                "Business",
                "Covers management, marketing, and entrepreneurship. Ideal for future leaders and business owners.",
                ContextCompat.getColor(requireContext(), R.color.olive_green),
            ),
            Department(
                "Finance",
                "Deals with investments, banking, and financial planning. Essential for understanding economic markets.",
                ContextCompat.getColor(requireContext(), R.color.olive_green),
            ),
            Department(
                "Law",
                "Studies legal systems, ethics, and justice. Prepares students for legal practice and advocacy.",
                ContextCompat.getColor(requireContext(), R.color.olive_green),
            ),
            Department(
                "Psychology",
                "Explores human behavior and the mind. Useful for counseling, research, and social work.",
                ContextCompat.getColor(requireContext(), R.color.olive_green),
            ),
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = DepartmentAdapter(departments) { department ->
            viewModel.selectDepartment(department)
            slidingPaneLayout.openPane()
        }
    }
}