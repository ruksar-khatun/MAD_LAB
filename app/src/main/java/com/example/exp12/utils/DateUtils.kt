package com.example.exp12.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    fun formatDate(rawDate: String?): String {
        if (rawDate.isNullOrBlank()) return "Just now"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val date: Date? = inputFormat.parse(rawDate)
            if (date != null) {
                getRelativeTimeSpan(date)
            } else {
                "Just now"
            }
        } catch (e: Exception) {
            try {
                val inputFormatAlt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                val dateAlt = inputFormatAlt.parse(rawDate)
                if (dateAlt != null) {
                    getRelativeTimeSpan(dateAlt)
                } else {
                    "Just now"
                }
            } catch (ex: Exception) {
                "Just now"
            }
        }
    }

    private fun getRelativeTimeSpan(date: Date): String {
        val now = System.currentTimeMillis()
        val diffMillis = now - date.time

        if (diffMillis < 0) {
            val outputFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
            return outputFormat.format(date)
        }

        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 2 -> "Just now"
            minutes < 60 -> "$minutes mins ago"
            hours < 2 -> "1 hour ago"
            hours < 24 -> "$hours hours ago"
            days < 2 -> "Yesterday"
            days < 7 -> "$days days ago"
            else -> {
                val outputFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                outputFormat.format(date)
            }
        }
    }
}
