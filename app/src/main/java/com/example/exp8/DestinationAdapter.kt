package com.example.exp8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.exp8.databinding.ItemDestinationBinding

class DestinationAdapter(
    private val onItemClick: (Destination) -> Unit,
    private val onItemLongClick: (Destination, View) -> Unit,
    private val onOverflowClick: (Destination, View) -> Unit
) : ListAdapter<Destination, DestinationAdapter.DestinationViewHolder>(DestinationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding = ItemDestinationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DestinationViewHolder(
        private val binding: ItemDestinationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(destination: Destination) {
            val context = binding.root.context

            binding.tvName.text = destination.name
            binding.tvLocation.text = destination.location
            binding.tvDescription.text = destination.description

            // Clear previous Glide target on recycled views
            Glide.with(context).clear(binding.ivDestination)

            // Image loading: local drawable resources via setImageResource, online images via Glide
            if (destination.isLocal && (destination.drawableResId != null)) {
                binding.ivDestination.setImageResource(destination.drawableResId)
            } else if (!destination.imageUrl.isNullOrEmpty()) {
                val glideUrl = GlideUrl(
                    destination.imageUrl,
                    LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                        .build()
                )

                Glide.with(context)
                    .load(glideUrl)
                    .override(400, 300)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(binding.ivDestination)
            } else {
                binding.ivDestination.setImageResource(R.drawable.ic_placeholder)
            }

            // Favourite indicator
            binding.ivFavouriteIcon.visibility = if (destination.isFavourite) View.VISIBLE else View.GONE

            // Selection state styling
            if (destination.isSelected) {
                binding.cardDestination.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_selected_bg))
                binding.cardDestination.strokeColor = ContextCompat.getColor(context, R.color.card_stroke_selected)
                binding.cardDestination.strokeWidth = 4
            } else {
                binding.cardDestination.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface))
                binding.cardDestination.strokeColor = ContextCompat.getColor(context, R.color.card_stroke)
                binding.cardDestination.strokeWidth = 1
            }

            // Click Listeners
            binding.root.setOnClickListener {
                onItemClick(destination)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick(destination, binding.root)
                true
            }

            binding.ibOverflow.setOnClickListener { view ->
                onOverflowClick(destination, view)
            }
        }
    }

    private class DestinationDiffCallback : DiffUtil.ItemCallback<Destination>() {
        override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean {
            return oldItem == newItem
        }
    }
}