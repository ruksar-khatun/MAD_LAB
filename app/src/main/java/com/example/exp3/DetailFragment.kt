package com.example.exp3

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel: DepartmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // --- PLACE BREAKPOINT HERE ---
        // Observe local variables like 'view' and the call stack.

        val container = view.findViewById<LinearLayout>(R.id.detailContainer)
        val nameTextView = view.findViewById<TextView>(R.id.detailName)
        val descriptionTextView = view.findViewById<TextView>(R.id.detailDescription)

        viewModel.selectedDepartment.observe(viewLifecycleOwner) { department ->
            nameTextView.text = department.name
            descriptionTextView.text = department.description
            container.setBackgroundColor(department.backgroundColor)
        }
    }
}
