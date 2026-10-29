package com.example.exp11.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import java.util.UUID

class DataRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("tour_pulse_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DESTINATIONS = "key_destinations"
        private const val KEY_ITINERARY = "key_itinerary"
        private const val KEY_TOURS = "key_tours"
        private const val KEY_FIRST_RUN = "key_first_run_tourist_v3"
    }

    init {
        if (prefs.getBoolean(KEY_FIRST_RUN, true)) {
            loadInitialFamousLocations()
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
        }
    }

    private fun loadInitialFamousLocations() {
        val sampleDestinations = listOf(
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Eiffel Tower",
                city = "Paris",
                country = "France",
                category = "Wonders",
                rating = 4.9,
                ratingCount = 3820,
                description = "An iconic wrought-iron lattice tower on the Champ de Mars in Paris. World-renowned symbol of romance and French culture.",
                highlights = "Champ de Mars view, Summit panorama, Light show at night",
                bestTime = "May - Sep",
                costLevel = "$$$",
                lat = 48.8584,
                lng = 2.2945,
                isFavorite = true,
                isVisited = true
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Taj Mahal",
                city = "Agra",
                country = "India",
                category = "Wonders",
                rating = 4.9,
                ratingCount = 4150,
                description = "An immense mausoleum of white marble, built in Agra by Mughal emperor Shah Jahan in memory of his favourite wife Mumtaz Mahal.",
                highlights = "Sunrise reflection pool, Marble calligraphy, Mehtab Bagh view",
                bestTime = "Oct - Mar",
                costLevel = "$$",
                lat = 27.1751,
                lng = 78.0421,
                isFavorite = true,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Colosseum",
                city = "Rome",
                country = "Italy",
                category = "Historical",
                rating = 4.8,
                ratingCount = 2940,
                description = "An ancient oval amphitheatre in the centre of the city of Rome, the largest ancient amphitheatre ever built.",
                highlights = "Gladiator underground chambers, Roman Forum walk, Night tours",
                bestTime = "Apr - Jun / Sep - Oct",
                costLevel = "$$",
                lat = 41.8902,
                lng = 12.4922,
                isFavorite = false,
                isVisited = true
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Machu Picchu",
                city = "Cusco Region",
                country = "Peru",
                category = "Wonders",
                rating = 4.9,
                ratingCount = 1890,
                description = "A 15th-century Inca citadel located in the Eastern Cordillera of southern Peru on a 2,430-metre mountain ridge.",
                highlights = "Inca Trail, Sun Gate view, Huayna Picchu climb",
                bestTime = "May - Oct",
                costLevel = "$$$",
                lat = -13.1631,
                lng = -72.5450,
                isFavorite = true,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Mount Fuji",
                city = "Honshu Island",
                country = "Japan",
                category = "Nature",
                rating = 4.8,
                ratingCount = 2100,
                description = "An active volcano about 100 kilometers south-west of Tokyo, Japan's tallest peak and sacred cultural icon.",
                highlights = "Lake Kawaguchiko views, Chureito Pagoda, Summit sunrise hike",
                bestTime = "Jul - Sep",
                costLevel = "$$",
                lat = 35.3606,
                lng = 138.7274,
                isFavorite = false,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Great Wall of China",
                city = "Beijing",
                country = "China",
                category = "Historical",
                rating = 4.9,
                ratingCount = 3100,
                description = "A series of fortifications that were built across the historical northern borders of ancient Chinese states.",
                highlights = "Mutianyu cable car, Badaling section, Watchtower views",
                bestTime = "Sep - Nov",
                costLevel = "$$",
                lat = 40.4319,
                lng = 116.5704,
                isFavorite = true,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Statue of Liberty",
                city = "New York",
                country = "USA",
                category = "Modern",
                rating = 4.7,
                ratingCount = 2400,
                description = "A colossal neoclassical sculpture on Liberty Island in New York Harbor, a gift from the people of France.",
                highlights = "Crown observatory access, Ellis Island museum, Harbor ferry ride",
                bestTime = "Apr - Jun / Sep - Nov",
                costLevel = "$$",
                lat = 40.6892,
                lng = -74.0445,
                isFavorite = false,
                isVisited = true
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Santorini Caldera",
                city = "Cyclades",
                country = "Greece",
                category = "Beaches",
                rating = 4.9,
                ratingCount = 2750,
                description = "Famous volcanic caldera island in the Aegean Sea known for whitewashed houses with blue domes and dramatic sunsets.",
                highlights = "Oia sunset view, Red Beach, Volcano boat tour",
                bestTime = "May - Oct",
                costLevel = "$$$",
                lat = 36.3932,
                lng = 25.4615,
                isFavorite = true,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Pyramids of Giza",
                city = "Cairo",
                country = "Egypt",
                category = "Historical",
                rating = 4.8,
                ratingCount = 3500,
                description = "Ancient pyramid complex near Cairo including the Great Pyramid of Giza and the Great Sphinx.",
                highlights = "Great Pyramid interior, Sphinx photo ops, Camel desert trek",
                bestTime = "Oct - Apr",
                costLevel = "$$",
                lat = 29.9792,
                lng = 31.1342,
                isFavorite = false,
                isVisited = false
            ),
            DestinationItem(
                id = UUID.randomUUID().toString(),
                name = "Sydney Opera House",
                city = "Sydney",
                country = "Australia",
                category = "Modern",
                rating = 4.8,
                ratingCount = 1950,
                description = "A multi-venue performing arts centre in Sydney Harbor, widely regarded as one of the world's most famous structures.",
                highlights = "Harbour Bridge walk, Opera performance, Circular Quay dining",
                bestTime = "Sep - Nov / Feb - Apr",
                costLevel = "$$$",
                lat = -33.8568,
                lng = 151.2153,
                isFavorite = false,
                isVisited = false
            )
        )

        val sampleItinerary = listOf(
            ItineraryItem(
                id = UUID.randomUUID().toString(),
                locationName = "Eiffel Tower Sunset & Seine Cruise",
                cityCountry = "Paris, France",
                plannedDate = "Completed",
                category = "Wonders",
                notes = "Booked evening ticket at 7 PM to catch sparkling light show",
                isVisited = true
            ),
            ItineraryItem(
                id = UUID.randomUUID().toString(),
                locationName = "Sunrise Guided Walk at Taj Mahal",
                cityCountry = "Agra, India",
                plannedDate = "Nov 2025",
                category = "Wonders",
                notes = "Arrive at East Gate by 5:30 AM for morning reflections",
                isVisited = false
            ),
            ItineraryItem(
                id = UUID.randomUUID().toString(),
                locationName = "Colosseum Underground Gladiators Tour",
                cityCountry = "Rome, Italy",
                plannedDate = "Completed",
                category = "Historical",
                notes = "Visited ancient Roman Forum and Palatine Hill",
                isVisited = true
            ),
            ItineraryItem(
                id = UUID.randomUUID().toString(),
                locationName = "Machu Picchu 4-Day Inca Trail Hike",
                cityCountry = "Cusco, Peru",
                plannedDate = "May 2026",
                category = "Wonders",
                notes = "Reserve trail permit 6 months in advance",
                isVisited = false
            ),
            ItineraryItem(
                id = UUID.randomUUID().toString(),
                locationName = "Chureito Pagoda Fuji Photo Spot",
                cityCountry = "Yamanashi, Japan",
                plannedDate = "Oct 2025",
                category = "Nature",
                notes = "Climb 398 steps for panoramic cherry blossom / Fuji view",
                isVisited = false
            )
        )

        val sampleTours = listOf(
            CityTour(
                id = UUID.randomUUID().toString(),
                cityName = "Rome, Italy",
                tourTitle = "Imperial Rome & Colosseum Walk",
                durationHours = 2.5,
                distanceKm = 3.8,
                totalStops = 6,
                description = "Walk through ancient history starting at the Colosseum, past the Arch of Constantine to the Roman Forum and Capitoline Hill.",
                currentStopIndex = 1,
                isPlaying = false
            ),
            CityTour(
                id = UUID.randomUUID().toString(),
                cityName = "Paris, France",
                tourTitle = "Historic Heart of Paris & Seine Trail",
                durationHours = 3.0,
                distanceKm = 4.5,
                totalStops = 7,
                description = "Discover Notre-Dame, Sainte-Chapelle, Pont Neuf, and end at Louvre Pyramid with audio commentary.",
                currentStopIndex = 2,
                isPlaying = false
            ),
            CityTour(
                id = UUID.randomUUID().toString(),
                cityName = "Tokyo & Kyoto, Japan",
                tourTitle = "Sacred Temples & Cherry Blossom Path",
                durationHours = 2.0,
                distanceKm = 3.2,
                totalStops = 5,
                description = "Explore Senso-ji Temple, Meiji Shrine, and traditional Gion geisha district.",
                currentStopIndex = 1,
                isPlaying = false
            )
        )

        saveDestinations(sampleDestinations)
        saveItinerary(sampleItinerary)
        saveTours(sampleTours)
    }

    // DESTINATIONS
    fun getDestinations(): MutableList<DestinationItem> {
        val jsonString = prefs.getString(KEY_DESTINATIONS, null) ?: return mutableListOf()
        val list = mutableListOf<DestinationItem>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(DestinationItem.fromJsonObject(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveDestinations(items: List<DestinationItem>) {
        val jsonArray = JSONArray()
        items.forEach { jsonArray.put(it.toJsonObject()) }
        prefs.edit().putString(KEY_DESTINATIONS, jsonArray.toString()).apply()
    }

    fun addDestination(item: DestinationItem) {
        val current = getDestinations()
        current.add(0, item)
        saveDestinations(current)
    }

    fun updateDestination(item: DestinationItem) {
        val current = getDestinations()
        val index = current.indexOfFirst { it.id == item.id }
        if (index != -1) {
            current[index] = item
            saveDestinations(current)
        }
    }

    fun deleteDestination(id: String) {
        val current = getDestinations()
        current.removeAll { it.id == id }
        saveDestinations(current)
    }

    // ITINERARY / BUCKET LIST
    fun getItinerary(): MutableList<ItineraryItem> {
        val jsonString = prefs.getString(KEY_ITINERARY, null) ?: return mutableListOf()
        val list = mutableListOf<ItineraryItem>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(ItineraryItem.fromJsonObject(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveItinerary(items: List<ItineraryItem>) {
        val jsonArray = JSONArray()
        items.forEach { jsonArray.put(it.toJsonObject()) }
        prefs.edit().putString(KEY_ITINERARY, jsonArray.toString()).apply()
    }

    fun addItineraryItem(item: ItineraryItem) {
        val current = getItinerary()
        current.add(0, item)
        saveItinerary(current)
    }

    fun updateItineraryItem(item: ItineraryItem) {
        val current = getItinerary()
        val index = current.indexOfFirst { it.id == item.id }
        if (index != -1) {
            current[index] = item
            saveItinerary(current)
        }
    }

    fun deleteItineraryItem(id: String) {
        val current = getItinerary()
        current.removeAll { it.id == id }
        saveItinerary(current)
    }

    // TOURS
    fun getTours(): MutableList<CityTour> {
        val jsonString = prefs.getString(KEY_TOURS, null) ?: return mutableListOf()
        val list = mutableListOf<CityTour>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(CityTour.fromJsonObject(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveTours(tours: List<CityTour>) {
        val jsonArray = JSONArray()
        tours.forEach { jsonArray.put(it.toJsonObject()) }
        prefs.edit().putString(KEY_TOURS, jsonArray.toString()).apply()
    }

    fun updateTour(tour: CityTour) {
        val current = getTours()
        val index = current.indexOfFirst { it.id == tour.id }
        if (index != -1) {
            current[index] = tour
            saveTours(current)
        }
    }
}
