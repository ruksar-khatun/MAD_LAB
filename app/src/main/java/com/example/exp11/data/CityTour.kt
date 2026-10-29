package com.example.exp11.data

import org.json.JSONObject

data class CityTour(
    val id: String,
    var cityName: String,
    var tourTitle: String,
    var durationHours: Double = 2.5,
    var distanceKm: Double = 4.2,
    var totalStops: Int = 6,
    var description: String = "",
    var currentStopIndex: Int = 1,
    var isPlaying: Boolean = false
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("cityName", cityName)
            put("tourTitle", tourTitle)
            put("durationHours", durationHours)
            put("distanceKm", distanceKm)
            put("totalStops", totalStops)
            put("description", description)
            put("currentStopIndex", currentStopIndex)
            put("isPlaying", isPlaying)
        }
    }

    companion object {
        fun fromJsonObject(json: JSONObject): CityTour {
            return CityTour(
                id = json.optString("id", System.currentTimeMillis().toString()),
                cityName = json.optString("cityName", ""),
                tourTitle = json.optString("tourTitle", ""),
                durationHours = json.optDouble("durationHours", 2.0),
                distanceKm = json.optDouble("distanceKm", 3.5),
                totalStops = json.optInt("totalStops", 5),
                description = json.optString("description", ""),
                currentStopIndex = json.optInt("currentStopIndex", 1),
                isPlaying = json.optBoolean("isPlaying", false)
            )
        }
    }
}
