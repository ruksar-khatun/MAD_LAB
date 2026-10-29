package com.example.exp11.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exp11.data.DataRepository
import com.example.exp11.data.ItineraryItem
import com.example.exp11.databinding.DialogAddItineraryBinding
import com.example.exp11.databinding.FragmentItineraryBinding
import com.example.exp11.ui.adapters.ItineraryAdapter
import com.google.android.material.button.MaterialButton
import java.util.UUID

class ItineraryFragment : Fragment() {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DataRepository
    private lateinit var adapter: ItineraryAdapter
    private var itineraryList: MutableList<ItineraryItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DataRepository(requireContext())

        setupRecyclerView()
        setupListeners()
        loadItinerary()
    }

    private fun setupRecyclerView() {
        adapter = ItineraryAdapter(
            items = listOf(),
            onVisitedToggle = { item, isChecked ->
                item.isVisited = isChecked
                repository.updateItineraryItem(item)
                updateSummary()
            },
            onDeleteClick = { item ->
                repository.deleteItineraryItem(item.id)
                loadItinerary()
                Toast.makeText(requireContext(), "Item removed", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvItinerary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItinerary.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAddItinerary.setOnClickListener {
            showAddItineraryDialog()
        }
    }

    fun loadItinerary() {
        itineraryList = repository.getItinerary()
        adapter.updateData(itineraryList)

        if (itineraryList.isEmpty()) {
            binding.tvEmptyItinerary.visibility = View.VISIBLE
            binding.rvItinerary.visibility = View.GONE
        } else {
            binding.tvEmptyItinerary.visibility = View.GONE
            binding.rvItinerary.visibility = View.VISIBLE
        }

        updateSummary()
    }

    private fun updateSummary() {
        val total = itineraryList.size
        val visited = itineraryList.count { it.isVisited }
        binding.tvBucketSummaryText.text = "$visited of $total dream places visited ✈️"
        binding.progressBucket.max = if (total > 0) total else 1
        binding.progressBucket.progress = visited
    }

    private fun showAddItineraryDialog() {
        val dialogBinding = DialogAddItineraryBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        val parentView = dialogBinding.root
        val btnSave = MaterialButton(requireContext()).apply {
            text = "Add to Bucket List"
            setOnClickListener {
                val name = dialogBinding.etItineraryName.text?.toString()?.trim() ?: ""
                val city = dialogBinding.etItineraryCity.text?.toString()?.trim() ?: ""
                val notes = dialogBinding.etItineraryNotes.text?.toString()?.trim() ?: ""

                if (name.isEmpty()) {
                    dialogBinding.etItineraryName.error = "Name required"
                    return@setOnClickListener
                }

                val newItem = ItineraryItem(
                    id = UUID.randomUUID().toString(),
                    locationName = name,
                    cityCountry = city,
                    plannedDate = "Upcoming 2025",
                    category = "Wonders",
                    notes = notes,
                    isVisited = false
                )

                repository.addItineraryItem(newItem)
                loadItinerary()
                dialog.dismiss()
                Toast.makeText(requireContext(), "Added to Bucket List!", Toast.LENGTH_SHORT).show()
            }
        }

        parentView.addView(btnSave)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
