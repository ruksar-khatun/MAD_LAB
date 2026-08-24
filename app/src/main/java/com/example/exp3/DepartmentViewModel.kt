package com.example.exp3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DepartmentViewModel : ViewModel() {
    private val _selectedDepartment = MutableLiveData<Department>()
    val selectedDepartment: LiveData<Department> get() = _selectedDepartment

    fun selectDepartment(department: Department) {
        _selectedDepartment.value = department
    }
}
