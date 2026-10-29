package com.example.exp11.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exp11.R
import com.example.exp11.data.DataRepository
import com.example.exp11.data.DestinationItem
import com.example.exp11.data.ItineraryItem
import com.example.exp11.databinding.DialogAddDestinationBinding
import com.example.exp11.databinding.FragmentExploreBinding
import com.example.exp11.ui.adapters.DestinationAdapter
import com.google.android.material.button.MaterialButton
import java.util.UUID

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DataRepository
    private lateinit var adapter: DestinationAdapter

    private var allDestinations: MutableList<DestinationItem> = mutableListOf()
    private var selectedCategory: String = "All"
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DataRepository(requireContext())

        setupRecyclerView()
        setupListeners()
        loadDestinations()
    }

    private fun setupRecyclerView() {
        adapter = DestinationAdapter(
            destinations = listOf(),
            onFavoriteClick = { item ->
                item.isFavorite = !item.isFavorite
                repository.updateDestination(item)
                loadDestinations()
            },
            onAddToBucketList = { item ->
                val newItem = ItineraryItem(
                    id = UUID.randomUUID().toString(),
                    locationName = item.name,
                    cityCountry = "${item.city}, ${item.country}",
                    plannedDate = "Upcoming 2025",
                    category = item.category,
                    notes = item.highlights,
                    isVisited = false
                )
                repository.addItineraryItem(newItem)
                Toast.makeText(requireContext(), "Added ${item.name} to Bucket List! ✈️", Toast.LENGTH_SHORT).show()
            },
            onOpenMapClick = { item ->
                openLocationInGoogleMaps(item)
            }
        )
        binding.rvDestinations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDestinations.adapter = adapter
    }

    private fun openLocationInGoogleMaps(item: DestinationItem) {
        val geoUri = Uri.parse("geo:${item.lat},${item.lng}?q=${item.lat},${item.lng}(${Uri.encode(item.name)})")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        try {
            startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback to browser Google Maps
            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${item.lat},${item.lng}")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            startActivity(webIntent)
        }
    }

    private fun setupListeners() {
        binding.fabAddDestination.setOnClickListener {
            showAddDestinationDialog()
        }

        binding.etSearchLocations.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString()?.trim() ?: ""
                filterDestinations()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.chipGroupExploreCat.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedCategory = if (checkedIds.isEmpty()) {
                "All"
            } else {
                when (checkedIds[0]) {
                    R.id.chipWonders -> "Wonders"
                    R.id.chipHistorical -> "Historical"
                    R.id.chipNature -> "Nature"
                    R.id.chipBeaches -> "Beaches"
                    R.id.chipModern -> "Modern"
                    else -> "All"
                }
            }
            filterDestinations()
        }
    }

    fun loadDestinations() {
        allDestinations = repository.getDestinations()
        filterDestinations()
    }

    private fun filterDestinations() {
        val filtered = allDestinations.filter { item ->
            val matchesCat = if (selectedCategory == "All") true else item.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = if (searchQuery.isEmpty()) true else {
                item.name.contains(searchQuery, ignoreCase = true) ||
                        item.city.contains(searchQuery, ignoreCase = true) ||
                        item.country.contains(searchQuery, ignoreCase = true)
            }
            matchesCat && matchesSearch
        }

        adapter.updateData(filtered)

        if (filtered.isEmpty()) {
            binding.tvEmptyDestinations.visibility = View.VISIBLE
            binding.rvDestinations.visibility = View.GONE
        } else {
            binding.tvEmptyDestinations.visibility = View.GONE
            binding.rvDestinations.visibility = View.VISIBLE
        }

        binding.tvExploreSubtitle.text = "Discover ${filtered.size} iconic famous locations"
    }

    private fun showAddDestinationDialog() {
        val dialogBinding = DialogAddDestinationBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        var selectedCat = "Wonders"

        dialogBinding.dialogCategoryGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                selectedCat = when (checkedIds[0]) {
                    R.id.dialogCatHistorical -> "Historical"
                    R.id.dialogCatNature -> "Nature"
                    R.id.dialogCatBeaches -> "Beaches"
                    R.id.dialogCatModern -> "Modern"
                    else -> "Wonders"
                }
            }
        }

        val parentView = dialogBinding.root
        val btnSave = MaterialButton(requireContext()).apply {
            text = getString(R.string.save)
            setOnClickListener {
                val name = dialogBinding.etDestName.text?.toString()?.trim() ?: ""
                val cityCountry = dialogBinding.etDestCityCountry.text?.toString()?.trim() ?: ""
                val desc = dialogBinding.etDestDesc.text?.toString()?.trim() ?: ""

                if (name.isEmpty()) {
                    dialogBinding.etDestName.error = "Name required"
                    return@setOnClickListener
                }

                val parts = cityCountry.split(",")
                val city = parts.firstOrNull()?.trim() ?: "Famous Spot"
                val country = if (parts.size > 1) parts[1].trim() else "World"

                val newItem = DestinationItem(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    city = city,
                    country = country,
                    category = selectedCat,
                    rating = 4.8,
                    ratingCount = 100,
                    description = desc,
                    highlights = "Custom landmark",
                    bestTime = "Year Round",
                    costLevel = "$$",
                    lat = 48.8584,
                    lng = 2.2945,
                    isFavorite = false,
                    isVisited = false
                )

                repository.addDestination(newItem)
                loadDestinations()
                dialog.dismiss()
                Toast.makeText(requireContext(), "Location added!", Toast.LENGTH_SHORT).show()
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
