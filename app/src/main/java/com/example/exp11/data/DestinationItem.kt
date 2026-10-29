package com.example.exp11.data

import org.json.JSONObject

data class DestinationItem(
    val id: String,
    var name: String,
    var city: String,
    var country: String,
    var category: String = "Wonders", // Wonders, Historical, Nature, Beaches, Modern
    var rating: Double = 4.8,
    var ratingCount: Int = 1250,
    var description: String = "",
    var highlights: String = "",
    var bestTime: String = "Apr - Oct",
    var costLevel: String = "$$$",
    var lat: Double = 48.8584,
    var lng: Double = 2.2945,
    var isFavorite: Boolean = false,
    var isVisited: Boolean = false
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
            put("city", city)
            put("country", country)
            put("category", category)
            put("rating", rating)
            put("ratingCount", ratingCount)
            put("description", description)
            put("highlights", highlights)
            put("bestTime", bestTime)
            put("costLevel", costLevel)
            put("lat", lat)
            put("lng", lng)
            put("isFavorite", isFavorite)
            put("isVisited", isVisited)
        }
    }

    companion object {
        fun fromJsonObject(json: JSONObject): DestinationItem {
            return DestinationItem(
                id = json.optString("id", System.currentTimeMillis().toString()),
                name = json.optString("name", ""),
                city = json.optString("city", ""),
                country = json.optString("country", ""),
                category = json.optString("category", "Wonders"),
                rating = json.optDouble("rating", 4.8),
                ratingCount = json.optInt("ratingCount", 500),
                description = json.optString("description", ""),
                highlights = json.optString("highlights", ""),
                bestTime = json.optString("bestTime", "Spring / Autumn"),
                costLevel = json.optString("costLevel", "$$"),
                lat = json.optDouble("lat", 48.8584),
                lng = json.optDouble("lng", 2.2945),
                isFavorite = json.optBoolean("isFavorite", false),
                isVisited = json.optBoolean("isVisited", false)
            )
        }
    }
}
