package com.example.exp4

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel: DepartmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.detailName)
        val descriptionTextView = view.findViewById<TextView>(R.id.detailDescription)

        viewModel.selectedDepartment.observe(viewLifecycleOwner) { department ->
            nameTextView.text = department.name
            descriptionTextView.text = department.description
        }
    }
}