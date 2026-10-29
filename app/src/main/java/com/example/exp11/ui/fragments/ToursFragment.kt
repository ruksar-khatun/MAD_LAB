package com.example.exp11.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exp11.R
import com.example.exp11.data.CityTour
import com.example.exp11.data.DataRepository
import com.example.exp11.databinding.FragmentToursBinding
import com.example.exp11.ui.adapters.TourAdapter

class ToursFragment : Fragment() {

    private var _binding: FragmentToursBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DataRepository
    private lateinit var adapter: TourAdapter
    private var toursList: MutableList<CityTour> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DataRepository(requireContext())

        setupRecyclerView()
        setupListeners()
        loadTours()
    }

    private fun setupRecyclerView() {
        adapter = TourAdapter(
            tours = listOf(),
            onPlayClick = { tour ->
                val newPlayState = !tour.isPlaying
                // Pause all others
                toursList.forEach { it.isPlaying = false }
                tour.isPlaying = newPlayState
                repository.saveTours(toursList)
                loadTours()

                if (newPlayState) {
                    Toast.makeText(requireContext(), "Playing audio guide for ${tour.cityName} 🎧", Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.rvTours.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTours.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnActivePause.setOnClickListener {
            val activeTour = toursList.firstOrNull { it.isPlaying }
            if (activeTour != null) {
                activeTour.isPlaying = false
                repository.saveTours(toursList)
                loadTours()
            }
        }
    }

    fun loadTours() {
        toursList = repository.getTours()
        adapter.updateData(toursList)

        val activeTour = toursList.firstOrNull { it.isPlaying }
        if (activeTour != null) {
            binding.cardActiveAudioPlayer.visibility = View.VISIBLE
            binding.tvActiveTourTitle.text = "${activeTour.cityName}: ${activeTour.tourTitle}"
            binding.tvActiveTourStatus.text = "Stop ${activeTour.currentStopIndex} of ${activeTour.totalStops} • Playing audio guide..."
            binding.btnActivePause.setIconResource(R.drawable.ic_pause)
        } else {
            binding.cardActiveAudioPlayer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
