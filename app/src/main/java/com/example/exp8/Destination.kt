package com.example.exp8

data class Destination(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val isLocal: Boolean,
    val drawableResId: Int? = null,
    val imageUrl: String? = null,
    val webUrl: String,
    var isFavourite: Boolean = false,
    var isSelected: Boolean = false
)