# Exp 11: Develop an Application to Display Google Maps.

## 📌 Overview
**TourPulse** is a feature-rich Android travel exploration application developed in Kotlin using Android Studio and Material 3 Design principles. The application showcases famous global landmarks (such as the *Eiffel Tower*, *Taj Mahal*, *Colosseum*, *Machu Picchu*, *Mount Fuji*, *Great Wall of China*, *Statue of Liberty*, *Santorini*, *Pyramids of Giza*, and *Sydney Opera House*). 

Each landmark card features a dedicated **Map Icon / Button (📍)**. Tapping this button launches an Android implicit intent using the `geo:` URI scheme to relocate directly to **Google Maps** at the exact geographical coordinates for turn-by-turn navigation and map visualization.

---

## 🎯 Core Requirements
1. **Google Maps Redirection:**
   - Integrated a dedicated Map Action Button (📍) for every location.
   - Triggers an implicit intent (`Intent.ACTION_VIEW` with `geo:lat,lng?q=lat,lng(Name)`) to launch the native Google Maps app, with an automatic web browser fallback.
2. **Famous Locations Showcase:**
   - Display world landmarks with category tags (*Wonders, Historical, Nature, Beaches, Modern*), star ratings, descriptions, best visiting seasons, and highlights.
3. **Interactive Search & Filtering:**
   - Real-time location search bar and interactive Material 3 Chip filtering.
4. **Travel Bucket List & Itinerary:**
   - Track planned travel spots with visited status toggles and progress indicators.
5. **Guided Audio City Tours:**
   - Explore self-guided city walking tours with play/pause audio guide controls.
6. **Traveler Passport & Analytics:**
   - Computes an overall World Explorer Score percentage and awards traveler rank badges (*Globetrotter 🌍*, *World Voyager 🧭*, *Exploring Rookie 🗺️*).
7. **Local Data Persistence:**
   - Manages state and demo content persistently using `SharedPreferences` with JSON serialization.

---

## 💻 Code Snippet

### Google Maps Relocation Intent (`ExploreFragment.kt`)
```kotlin
private fun openLocationInGoogleMaps(item: DestinationItem) {
    // Geo URI format with latitude, longitude and place name label
    val geoUri = Uri.parse("geo:${item.lat},${item.lng}?q=${item.lat},${item.lng}(${Uri.encode(item.name)})")
    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        startActivity(mapIntent)
    } catch (e: Exception) {
        // Fallback to Google Maps Web URL if Google Maps app is not installed
        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${item.lat},${item.lng}")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        startActivity(webIntent)
    }
}
```

### Destination Item Layout Action (`item_destination.xml`)
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnOpenMap"
    style="@style/Widget.Material3.Button.OutlinedButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Map"
    app:icon="@drawable/ic_location_pin"
    app:iconTint="@color/primary"
    app:strokeColor="@color/primary" />
```

---

## 📁 Project Structure

```
Exp11/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/exp11/
│   │   │   │   ├── data/
│   │   │   │   │   ├── DestinationItem.kt
│   │   │   │   │   ├── ItineraryItem.kt
│   │   │   │   │   ├── CityTour.kt
│   │   │   │   │   └── DataRepository.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── adapters/
│   │   │   │   │   │   ├── DestinationAdapter.kt
│   │   │   │   │   │   ├── ItineraryAdapter.kt
│   │   │   │   │   │   └── TourAdapter.kt
│   │   │   │   │   └── fragments/
│   │   │   │   │       ├── ExploreFragment.kt
│   │   │   │   │       ├── ItineraryFragment.kt
│   │   │   │   │       ├── ToursFragment.kt
│   │   │   │   │       └── PassportFragment.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_explore.xml
│   │   │   │   │   ├── ic_location_pin.xml
│   │   │   │   │   ├── ic_itinerary.xml
│   │   │   │   │   ├── ic_tours.xml
│   │   │   │   │   ├── ic_passport.xml
│   │   │   │   │   └── ic_star.xml
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── fragment_explore.xml
│   │   │   │   │   ├── item_destination.xml
│   │   │   │   │   ├── fragment_itinerary.xml
│   │   │   │   │   ├── fragment_tours.xml
│   │   │   │   │   └── fragment_passport.xml
│   │   │   │   ├── menu/
│   │   │   │   │   └── bottom_nav_menu.xml
│   │   │   │   └── values/
│   │   │   │       ├── colors.xml
│   │   │   │       └── strings.xml
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
├── img.jpeg
├── img1.jpeg
├── img2.jpeg
└── README.md
```

---

## 📱 Output & Application Screenshots

### 1. Explore Famous Locations Screen
Displays the list of famous global locations with ratings, category chips (*Wonders, Historical, Nature, etc.*), search bar, and action buttons including **Map (📍)** and **Bucket List (+)**.

![Explore Screen](img.jpeg)

*Description: The main explore interface showing world landmarks with search, category filtering, and direct Google Maps relocation controls.*

---

### 2. Google Maps Relocation Output
When the **Map (📍)** icon is clicked for any landmark (e.g., *Eiffel Tower* or *Taj Mahal*), the app executes the location intent and relocates directly to Google Maps showing the exact location and navigation route.

![Google Maps View](img1.jpeg)

*Description: Google Maps interface launched via Implicit Intent showing the exact geographic coordinates of the selected landmark.*

---

### 3. Travel Bucket List & Passport Dashboard
Displays the user's travel bucket list, visited progress bar, guided city audio tours, and the Traveler Passport with World Exploration Score analytics.

![Bucket List & Passport](img2.jpeg)

*Description: The Travel Bucket List and Traveler Passport interface displaying visited landmarks, completed tours, and exploration statistics.*
