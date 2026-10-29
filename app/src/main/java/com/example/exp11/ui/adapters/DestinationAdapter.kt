package com.example.exp11.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exp11.R
import com.example.exp11.data.DestinationItem
import com.example.exp11.databinding.ItemDestinationBinding

class DestinationAdapter(
    private var destinations: List<DestinationItem>,
    private val onFavoriteClick: (DestinationItem) -> Unit,
    private val onAddToBucketList: (DestinationItem) -> Unit,
    private val onOpenMapClick: (DestinationItem) -> Unit
) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    class DestinationViewHolder(val binding: ItemDestinationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding = ItemDestinationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val item = destinations[position]
        val binding = holder.binding

        binding.tvDestName.text = item.name
        binding.tvDestCityCountry.text = "${item.city}, ${item.country}"
        binding.tvDestCategory.text = item.category
        binding.tvDestDesc.text = item.description
        binding.tvDestRating.text = "${item.rating} (${item.ratingCount} reviews)"
        binding.tvBestTime.text = "Best: ${item.bestTime}"
        binding.tvHighlights.text = "✨ ${item.highlights}"

        if (item.isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_heart_outline)
        }

        binding.btnFavorite.setOnClickListener {
            onFavoriteClick(item)
        }

        binding.btnOpenMap.setOnClickListener {
            onOpenMapClick(item)
        }

        binding.btnAddBucketList.setOnClickListener {
            onAddToBucketList(item)
        }
    }

    override fun getItemCount(): Int = destinations.size

    fun updateData(newDestinations: List<DestinationItem>) {
        this.destinations = newDestinations
        notifyDataSetChanged()
    }
}
