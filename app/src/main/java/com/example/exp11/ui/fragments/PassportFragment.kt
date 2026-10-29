package com.example.exp11.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.exp11.data.DataRepository
import com.example.exp11.databinding.FragmentPassportBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PassportFragment : Fragment() {

    private var _binding: FragmentPassportBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DataRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DataRepository(requireContext())

        setupDateHeader()
        loadPassportData()
    }

    private fun setupDateHeader() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvPassportSubtitle.text = "Member since " + dateFormat.format(Date())
    }

    fun loadPassportData() {
        val destinations = repository.getDestinations()
        val itinerary = repository.getItinerary()
        val tours = repository.getTours()

        val totalDestinations = destinations.size
        val visitedDestinations = destinations.count { it.isVisited }
        val favoritesCount = destinations.count { it.isFavorite }

        val bucketTotal = itinerary.size
        val bucketVisited = itinerary.count { it.isVisited }

        val totalTours = tours.size

        binding.tvVisitedVal.text = "$visitedDestinations / $totalDestinations"
        binding.tvFavoritesVal.text = "$favoritesCount Saved"
        binding.tvBucketListVal.text = "$bucketVisited Completed"
        binding.tvAudioToursVal.text = "$totalTours Available"

        // Score calculation
        val destPct = if (totalDestinations > 0) (visitedDestinations * 100) / totalDestinations else 0
        val bucketPct = if (bucketTotal > 0) (bucketVisited * 100) / bucketTotal else 0
        val favScore = (favoritesCount * 10).coerceAtMost(100)

        val score = ((destPct * 0.4) + (bucketPct * 0.4) + (favScore * 0.2)).toInt().coerceIn(10, 99)
        binding.tvPassportScoreVal.text = "$score%"
        binding.progressPassport.progress = score

        binding.tvExplorerRank.text = when {
            score >= 70 -> "Globetrotter 🌍"
            score >= 40 -> "World Voyager 🧭"
            else -> "Exploring Rookie 🗺️"
        }
    }

    override fun onResume() {
        super.onResume()
        loadPassportData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
