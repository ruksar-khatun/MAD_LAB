package com.example.exp12.data.api

object Constants {
    // Primary NewsAPI.org Base URL
    const val NEWS_API_BASE_URL = "https://newsapi.org/v2/"

    // Free Open Mirror Base URL (Does NOT require API key, 100% reliable for lab tests)
    const val FALLBACK_BASE_URL = "https://saurav.tech/NewsAPI/"

    // Replace "YOUR_API_KEY_HERE" with your free API key from https://newsapi.org/
    const val API_KEY = "YOUR_API_KEY_HERE"

    const val DEFAULT_COUNTRY = "us"
    const val DEFAULT_CATEGORY = "general"
}
