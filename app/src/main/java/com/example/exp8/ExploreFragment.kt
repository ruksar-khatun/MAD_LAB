package com.example.exp8

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.exp8.databinding.DialogDestinationDetailsBinding
import com.example.exp8.databinding.DialogFullScreenImageBinding
import com.example.exp8.databinding.FragmentExploreBinding
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: DestinationAdapter

    private var contextMenuDestination: Destination? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = DestinationAdapter(
            onItemClick = { destination ->
                viewModel.toggleSelection(destination.id)
                showFullScreenImageViewer(destination)
            },
            onItemLongClick = { destination, view ->
                contextMenuDestination = destination
                viewModel.setSelectedDestination(destination)
                registerForContextMenu(view)
                requireActivity().openContextMenu(view)
            },
            onOverflowClick = { destination, anchorView ->
                showPopupMenu(destination, anchorView)
            }
        )

        binding.rvDestinations.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ExploreFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.destinations.observe(viewLifecycleOwner) { list ->
            filterAndSubmitList(list, viewModel.searchQuery.value ?: "")
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            val currentList = viewModel.destinations.value ?: emptyList()
            filterAndSubmitList(currentList, query)
        }
    }

    private fun filterAndSubmitList(list: List<Destination>, query: String) {
        val filtered = if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }

        adapter.submitList(filtered)
        if (filtered.isEmpty()) {
            binding.rvDestinations.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvDestinations.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }
    }

    private fun showPopupMenu(destination: Destination, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        val favItem = popup.menu.findItem(R.id.pop_toggle_favourite)
        favItem?.title = if (destination.isFavourite) {
            getString(R.string.menu_remove_favourite)
        } else {
            getString(R.string.menu_add_favourite)
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pop_view_details -> {
                    showDestinationDetailsDialog(destination)
                    true
                }
                R.id.pop_toggle_favourite -> {
                    viewModel.toggleFavourite(destination.id)
                    val msg = if (destination.isFavourite) {
                        "Removed from favourites"
                    } else {
                        "Added to favourites"
                    }
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.pop_share -> {
                    shareDestination(destination)
                    true
                }
                R.id.pop_open_web -> {
                    viewModel.openInWeb(destination.webUrl)
                    (activity as? MainActivity)?.navigateToTab(R.id.nav_web)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)

        val dest = contextMenuDestination ?: return
        menu.setHeaderTitle(dest.name)

        val favItem = menu.findItem(R.id.ctx_toggle_favourite)
        favItem?.title = if (dest.isFavourite) {
            getString(R.string.menu_remove_favourite)
        } else {
            getString(R.string.menu_add_favourite)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val dest = contextMenuDestination ?: return super.onContextItemSelected(item)

        return when (item.itemId) {
            R.id.ctx_view_details -> {
                showDestinationDetailsDialog(dest)
                true
            }
            R.id.ctx_share -> {
                shareDestination(dest)
                true
            }
            R.id.ctx_copy_name -> {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Destination Name", dest.name)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Copied '${dest.name}' to clipboard", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.ctx_open_location -> {
                openLocationInMaps(dest)
                true
            }
            R.id.ctx_toggle_favourite -> {
                viewModel.toggleFavourite(dest.id)
                val msg = if (dest.isFavourite) "Removed from favourites" else "Added to favourites"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showDestinationDetailsDialog(destination: Destination) {
        val dialogBinding = DialogDestinationDetailsBinding.inflate(layoutInflater)

        dialogBinding.tvDialogName.text = destination.name
        dialogBinding.tvDialogLocation.text = destination.location
        dialogBinding.tvDialogDescription.text = destination.description

        if (destination.isLocal && destination.drawableResId != null) {
            dialogBinding.ivDialogImage.setImageResource(destination.drawableResId)
        } else if (!destination.imageUrl.isNullOrEmpty()) {
            val glideUrl = GlideUrl(
                destination.imageUrl,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                    .build()
            )

            Glide.with(requireContext())
                .load(glideUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(dialogBinding.ivDialogImage)
        } else {
            dialogBinding.ivDialogImage.setImageResource(R.drawable.ic_placeholder)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_close, null)
            .create()

        dialogBinding.btnGeminiAi.setOnClickListener {
            dialogBinding.pbGemini.visibility = View.VISIBLE
            dialogBinding.btnGeminiAi.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                val insights = GeminiService.getDestinationInsights(destination.name, destination.location)
                dialogBinding.pbGemini.visibility = View.GONE
                dialogBinding.btnGeminiAi.isEnabled = true
                dialogBinding.tvGeminiInsights.text = insights
            }
        }

        dialogBinding.btnSearchPhotos.setOnClickListener {
            alertDialog.dismiss()
            val photoSearchUrl = "https://www.google.com/search?tbm=isch&q=${Uri.encode("${destination.name} ${destination.location} HD photographs")}"
            viewModel.openInWeb(photoSearchUrl)
            (activity as? MainActivity)?.navigateToTab(R.id.nav_web)
        }

        dialogBinding.btnDialogShare.setOnClickListener {
            shareDestination(destination)
        }

        dialogBinding.btnDialogWeb.setOnClickListener {
            alertDialog.dismiss()
            viewModel.openInWeb(destination.webUrl)
            (activity as? MainActivity)?.navigateToTab(R.id.nav_web)
        }

        dialogBinding.ivDialogImage.setOnClickListener {
            showFullScreenImageViewer(destination)
        }

        alertDialog.show()
    }

    private fun showFullScreenImageViewer(destination: Destination) {
        val dialogBinding = DialogFullScreenImageBinding.inflate(layoutInflater)

        dialogBinding.tvTitle.text = destination.name
        dialogBinding.tvLocation.text = destination.location

        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.btnBack.setOnClickListener {
            dialog.dismiss()
        }

        if (destination.isLocal && destination.drawableResId != null) {
            dialogBinding.pbLoading.visibility = View.GONE
            dialogBinding.ivFullScreen.setImageResource(destination.drawableResId)
        } else if (!destination.imageUrl.isNullOrEmpty()) {
            dialogBinding.pbLoading.visibility = View.VISIBLE
            dialogBinding.layoutError.visibility = View.GONE

            val glideUrl = GlideUrl(
                destination.imageUrl,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                    .build()
            )

            Glide.with(requireContext())
                .asBitmap()
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        if (_binding != null) {
                            dialogBinding.pbLoading.visibility = View.GONE
                            dialogBinding.ivFullScreen.setImageBitmap(resource)
                        }
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        if (_binding != null) {
                            dialogBinding.pbLoading.visibility = View.GONE
                            dialogBinding.layoutError.visibility = View.VISIBLE
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } else {
            dialogBinding.pbLoading.visibility = View.GONE
            dialogBinding.layoutError.visibility = View.VISIBLE
        }

        dialog.show()
    }

    private fun shareDestination(destination: Destination) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Explore ${destination.name} in ${destination.location}!\n\n${destination.description}\nRead more: ${destination.webUrl}"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share Destination"))
    }

    private fun openLocationInMaps(destination: Destination) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode("${destination.name}, ${destination.location}")}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            viewModel.openInWeb("https://www.google.com/maps/search/${Uri.encode("${destination.name} ${destination.location}")}")
            (activity as? MainActivity)?.navigateToTab(R.id.nav_web)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}