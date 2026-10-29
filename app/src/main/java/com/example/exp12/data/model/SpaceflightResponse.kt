package com.example.exp12.data.model

import com.google.gson.annotations.SerializedName

data class SpaceflightResponse(
    @SerializedName("count") val count: Int?,
    @SerializedName("results") val results: List<SpaceflightArticle>?
)

data class SpaceflightArticle(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("news_site") val newsSite: String?,
    @SerializedName("published_at") val publishedAt: String?
)
