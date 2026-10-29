package com.example.exp12.data.api

import com.example.exp12.data.model.NewsResponse
import com.example.exp12.data.model.OkSurfResponse
import com.example.exp12.data.model.SpaceflightResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {

    // Primary HTTP GET Request: Top headlines from NewsAPI.org
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = Constants.DEFAULT_COUNTRY,
        @Query("category") category: String = Constants.DEFAULT_CATEGORY,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    // Primary HTTP GET Request: Search articles from NewsAPI.org
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    // Real-Time Google News Feed API by OkSurf
    @GET
    suspend fun getOkSurfNewsFeed(@Url url: String): Response<OkSurfResponse>

    // Fallback Keyless REST Web Service: Spaceflight & Tech News API
    @GET
    suspend fun getSpaceflightNews(@Url url: String): Response<SpaceflightResponse>

    // Direct REST URL query for JSON payloads
    @GET
    suspend fun getNewsFromUrl(@Url url: String): Response<NewsResponse>
}
