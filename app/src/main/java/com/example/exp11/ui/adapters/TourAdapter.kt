package com.example.exp11.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exp11.R
import com.example.exp11.data.CityTour
import com.example.exp11.databinding.ItemTourBinding

class TourAdapter(
    private var tours: List<CityTour>,
    private val onPlayClick: (CityTour) -> Unit
) : RecyclerView.Adapter<TourAdapter.TourViewHolder>() {

    class TourViewHolder(val binding: ItemTourBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val binding = ItemTourBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tour = tours[position]
        val binding = holder.binding

        binding.tvTourCity.text = tour.cityName
        binding.tvTourDuration.text = "${tour.durationHours} Hrs • ${tour.distanceKm} km"
        binding.tvTourTitle.text = tour.tourTitle
        binding.tvTourDesc.text = tour.description
        binding.tvTourStops.text = "Stop ${tour.currentStopIndex} of ${tour.totalStops}"

        if (tour.isPlaying) {
            binding.btnPlayTour.text = "Pause"
            binding.btnPlayTour.setIconResource(R.drawable.ic_pause)
        } else {
            binding.btnPlayTour.text = "Start Tour"
            binding.btnPlayTour.setIconResource(R.drawable.ic_play)
        }

        binding.btnPlayTour.setOnClickListener {
            onPlayClick(tour)
        }
    }

    override fun getItemCount(): Int = tours.size

    fun updateData(newTours: List<CityTour>) {
        this.tours = newTours
        notifyDataSetChanged()
    }
}
