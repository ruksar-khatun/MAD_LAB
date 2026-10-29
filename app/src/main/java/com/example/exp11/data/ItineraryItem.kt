package com.example.exp11.data

import org.json.JSONObject

data class ItineraryItem(
    val id: String,
    var locationName: String,
    var cityCountry: String = "",
    var plannedDate: String = "Summer 2025",
    var category: String = "Wonders",
    var notes: String = "",
    var isVisited: Boolean = false
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("locationName", locationName)
            put("cityCountry", cityCountry)
            put("plannedDate", plannedDate)
            put("category", category)
            put("notes", notes)
            put("isVisited", isVisited)
        }
    }

    companion object {
        fun fromJsonObject(json: JSONObject): ItineraryItem {
            return ItineraryItem(
                id = json.optString("id", System.currentTimeMillis().toString()),
                locationName = json.optString("locationName", ""),
                cityCountry = json.optString("cityCountry", ""),
                plannedDate = json.optString("plannedDate", "Upcoming"),
                category = json.optString("category", "Wonders"),
                notes = json.optString("notes", ""),
                isVisited = json.optBoolean("isVisited", false)
            )
        }
    }
}
