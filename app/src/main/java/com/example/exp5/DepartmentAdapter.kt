package com.example.exp5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DepartmentAdapter(
    private val departments: List<Department>,
    private val onDepartmentClick: (Department) -> Unit
) : RecyclerView.Adapter<DepartmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.departmentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_department, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dept = departments[position]
        holder.nameText.text = dept.name
        holder.itemView.setOnClickListener {
            onDepartmentClick(dept)
        }
    }

    override fun getItemCount() = departments.size
}