package com.example.exp8

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiService {

    suspend fun getDestinationInsights(destinationName: String, location: String): String =
        withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GEMINI_API_KEY.trim()

            if (apiKey.isEmpty() || apiKey == "YOUR_GEMINI_API_KEY") {
                return@withContext getOfflineFallbackInsights(destinationName, location)
            }

            try {
                val endpoint =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
                val url = URL(endpoint)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 8000
                conn.readTimeout = 8000

                val promptText =
                    "Provide a concise historical overview (max 3 bullet points) and 3 image search terms for the heritage monument: $destinationName in $location."

                val jsonPayload = JSONObject().apply {
                    put("contents", listOf(
                        JSONObject().apply {
                            put("parts", listOf(
                                JSONObject().apply {
                                    put("text", promptText)
                                }
                            ))
                        }
                    ))
                }

                OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(jsonPayload.toString())
                    writer.flush()
                }

                if (conn.responseCode == 200) {
                    val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(responseText)
                    val candidates = jsonResponse.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text")
                            if (text.isNotBlank()) {
                                return@withContext "✨ Gemini AI Insights:\n\n$text"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext getOfflineFallbackInsights(destinationName, location)
        }

    private fun getOfflineFallbackInsights(destinationName: String, location: String): String {
        return "✨ Historical Insights ($destinationName):\n\n" +
                "• Located in $location, $destinationName is a world-renowned symbol of Indian cultural heritage.\n" +
                "• Features magnificent architectural craftsmanship attracting millions of global tourists.\n" +
                "• Suggested Google Search terms for photographs: '$destinationName photography HD', '$destinationName monument $location'."
    }
}