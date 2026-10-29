package com.example.exp12.ui

import com.example.exp12.data.model.Article

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>, val category: String) : NewsUiState()
    object Empty : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}
