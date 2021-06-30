package com.wolt.android.demo.data.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("venues")
    suspend fun search(
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double
    ): SearchResponse
}