package com.example.exp11.ui.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exp11.data.ItineraryItem
import com.example.exp11.databinding.ItemItineraryBinding

class ItineraryAdapter(
    private var items: List<ItineraryItem>,
    private val onVisitedToggle: (ItineraryItem, Boolean) -> Unit,
    private val onDeleteClick: (ItineraryItem) -> Unit
) : RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder>() {

    class ItineraryViewHolder(val binding: ItemItineraryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        val binding = ItemItineraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItineraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        val item = items[position]
        val binding = holder.binding

        binding.tvItineraryTitle.text = item.locationName
        binding.tvItineraryCity.text = item.cityCountry
        binding.tvItineraryNotes.text = "${item.plannedDate} • ${item.notes}"

        binding.cbVisited.setOnCheckedChangeListener(null)
        binding.cbVisited.isChecked = item.isVisited
        applyStrikethrough(binding, item.isVisited)

        binding.cbVisited.setOnCheckedChangeListener { _, isChecked ->
            applyStrikethrough(binding, isChecked)
            onVisitedToggle(item, isChecked)
        }

        binding.btnDeleteItinerary.setOnClickListener {
            onDeleteClick(item)
        }
    }

    private fun applyStrikethrough(binding: ItemItineraryBinding, isVisited: Boolean) {
        if (isVisited) {
            binding.tvItineraryTitle.paintFlags =
                binding.tvItineraryTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvItineraryTitle.alpha = 0.5f
            binding.tvItineraryNotes.alpha = 0.5f
        } else {
            binding.tvItineraryTitle.paintFlags =
                binding.tvItineraryTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            binding.tvItineraryTitle.alpha = 1.0f
            binding.tvItineraryNotes.alpha = 1.0f
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ItineraryItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
