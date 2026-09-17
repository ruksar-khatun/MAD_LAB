package com.example.exp7

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleSearchService {
    @GET("customsearch/v1")
    fun searchImage(
        @Query("q") query: String,
        @Query("searchType") searchType: String = "image",
        @Query("cx") cx: String,
        @Query("key") key: String,
        @Query("num") num: Int = 1
    ): Call<GoogleSearchResponse>
}
