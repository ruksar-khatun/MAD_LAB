package com.example.exp7

import com.google.gson.annotations.SerializedName

data class GoogleSearchResponse(
    @SerializedName("items") val items: List<SearchItem>?
)

data class SearchItem(
    @SerializedName("link") val link: String?
)
