package com.example.exp12.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exp12.data.model.Article
import com.example.exp12.data.repository.NewsRepository
import com.example.exp12.data.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SortOption {
    LATEST,
    OLDEST,
    TITLE_AZ
}

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _uiState = MutableLiveData<NewsUiState>()
    val uiState: LiveData<NewsUiState> get() = _uiState

    private var currentCategory: String = "india"
    private var currentQuery: String = ""
    private var currentArticles: List<Article> = emptyList()
    private var currentSortOption: SortOption = SortOption.LATEST

    init {
        loadNews(currentCategory)
    }

    fun loadNews(category: String, isRefresh: Boolean = false) {
        currentCategory = category
        currentQuery = ""
        fetchHeadlines(category, isRefresh)
    }

    fun searchNews(query: String) {
        currentQuery = query.trim()
        if (currentQuery.isBlank()) {
            loadNews(currentCategory)
            return
        }

        _uiState.value = NewsUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.searchNews(currentQuery, currentCategory)) {
                is Resource.Success -> {
                    currentArticles = result.data.orEmpty()
                    postSortedArticles(currentArticles)
                }
                is Resource.Error -> {
                    _uiState.postValue(NewsUiState.Error(result.message ?: "Failed to search news"))
                }
                is Resource.Loading -> {
                    _uiState.postValue(NewsUiState.Loading)
                }
            }
        }
    }

    fun sortNews(sortOption: SortOption) {
        currentSortOption = sortOption
        postSortedArticles(currentArticles)
    }

    private fun postSortedArticles(articles: List<Article>) {
        if (articles.isEmpty()) {
            _uiState.postValue(NewsUiState.Empty)
            return
        }

        val sorted = when (currentSortOption) {
            SortOption.LATEST -> articles
            SortOption.OLDEST -> articles.reversed()
            SortOption.TITLE_AZ -> articles.sortedBy { it.title?.lowercase() ?: "" }
        }

        _uiState.postValue(NewsUiState.Success(sorted, currentCategory))
    }

    fun refresh() {
        if (currentQuery.isNotBlank()) {
            searchNews(currentQuery)
        } else {
            fetchHeadlines(currentCategory, isRefresh = true)
        }
    }

    private fun fetchHeadlines(category: String, isRefresh: Boolean) {
        if (!isRefresh) {
            _uiState.value = NewsUiState.Loading
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getTopHeadlines(category)) {
                is Resource.Success -> {
                    currentArticles = result.data.orEmpty()
                    postSortedArticles(currentArticles)
                }
                is Resource.Error -> {
                    _uiState.postValue(NewsUiState.Error(result.message ?: "An unexpected error occurred"))
                }
                is Resource.Loading -> {
                    _uiState.postValue(NewsUiState.Loading)
                }
            }
        }
    }
}
