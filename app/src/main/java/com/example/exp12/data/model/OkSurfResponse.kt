package com.example.exp12.data.model

import com.google.gson.annotations.SerializedName

data class OkSurfResponse(
    @SerializedName("US") val topStories: List<OkSurfArticle>?,
    @SerializedName("Business") val business: List<OkSurfArticle>?,
    @SerializedName("Technology") val technology: List<OkSurfArticle>?,
    @SerializedName("Entertainment") val entertainment: List<OkSurfArticle>?,
    @SerializedName("Sports") val sports: List<OkSurfArticle>?,
    @SerializedName("Science") val science: List<OkSurfArticle>?,
    @SerializedName("Health") val health: List<OkSurfArticle>?,
    @SerializedName("World") val world: List<OkSurfArticle>?
)

data class OkSurfArticle(
    @SerializedName("title") val title: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("og") val ogImage: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("source_icon") val sourceIcon: String?
)
