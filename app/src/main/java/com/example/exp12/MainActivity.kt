package com.example.exp12

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exp12.data.model.Article
import com.example.exp12.data.repository.NewsRepository
import com.example.exp12.databinding.ActivityMainBinding
import com.example.exp12.ui.NewsAdapter
import com.example.exp12.ui.NewsUiState
import com.example.exp12.ui.NewsViewModel
import com.example.exp12.ui.NewsViewModelFactory
import com.example.exp12.ui.SortOption
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private var selectedSortIndex = 0

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(NewsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupCategoryChips()
        setupSearchInput()
        setupFilterButton()
        setupSwipeRefresh()
        setupErrorRetry()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            openArticleInBrowser(article)
        }

        binding.rvNewsList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
            setHasFixedSize(true)
        }
    }

    private fun openArticleInBrowser(article: Article) {
        val url = article.url
        if (!url.isNullOrBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open browser link", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Article URL unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener

            val category = when (checkedIds.first()) {
                R.id.chipIndia -> "india"
                R.id.chipWorld -> "world"
                R.id.chipGeneral -> "general"
                R.id.chipBusiness -> "business"
                R.id.chipTechnology -> "technology"
                R.id.chipEntertainment -> "entertainment"
                R.id.chipSports -> "sports"
                R.id.chipScience -> "science"
                R.id.chipHealth -> "health"
                else -> "india"
            }

            // Clear search field when switching category
            binding.etSearch.setText("")
            binding.etSearch.clearFocus()

            viewModel.loadNews(category)
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchNews(query)
                } else {
                    viewModel.refresh()
                }
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullByBlank()) {
                    // When user clears search text
                    viewModel.refresh()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun CharSequence?.isNullByBlank(): Boolean {
        return this == null || this.trim().isEmpty()
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            val filterOptions = arrayOf(
                getString(R.string.sort_latest),
                getString(R.string.sort_oldest),
                getString(R.string.sort_title)
            )

            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.filter_title))
                .setSingleChoiceItems(filterOptions, selectedSortIndex) { dialog, which ->
                    selectedSortIndex = which
                    val sortOption = when (which) {
                        0 -> SortOption.LATEST
                        1 -> SortOption.OLDEST
                        2 -> SortOption.TITLE_AZ
                        else -> SortOption.LATEST
                    }
                    viewModel.sortNews(sortOption)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.blue_accent)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupErrorRetry() {
        binding.btnRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            val wasRefreshing = binding.swipeRefreshLayout.isRefreshing
            binding.swipeRefreshLayout.isRefreshing = false

            when (state) {
                is NewsUiState.Loading -> {
                    showLoadingState()
                }
                is NewsUiState.Success -> {
                    showContentState(state.articles)
                    if (wasRefreshing) {
                        Toast.makeText(this, "Refreshed • News updated live", Toast.LENGTH_SHORT).show()
                    }
                }
                is NewsUiState.Empty -> {
                    showEmptyState()
                }
                is NewsUiState.Error -> {
                    showErrorState(state.message)
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvNewsList.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.GONE
        binding.layoutErrorState.visibility = View.GONE
    }

    private fun showContentState(articles: List<Article>) {
        binding.progressBar.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.GONE
        binding.layoutErrorState.visibility = View.GONE
        binding.rvNewsList.visibility = View.VISIBLE

        newsAdapter.submitList(articles)
    }

    private fun showEmptyState() {
        binding.progressBar.visibility = View.GONE
        binding.rvNewsList.visibility = View.GONE
        binding.layoutErrorState.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.VISIBLE
    }

    private fun showErrorState(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvNewsList.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.GONE
        binding.layoutErrorState.visibility = View.VISIBLE

        binding.tvErrorMessage.text = message
    }
}
