package com.wolt.android.demo.data.search

import com.wolt.android.demo.data.Place
import com.wolt.android.demo.data.Result
import com.wolt.android.demo.data.location.Location
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ISearchDataSource {
    suspend fun search(location: Location): Result<List<Place>>
}

class SearchDataSource(serverUrl: String) : ISearchDataSource {

    private var searchApi: SearchApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        searchApi = retrofit.create(SearchApi::class.java)
    }

    override suspend fun search(
        location: Location
    ): Result<List<Place>> {

        try {
            val response = searchApi.search(location.latitude, location.longitude)

            if (response.status != "OK") {
                return Result.Error(Exception(response.status))
            }

            return Result.Success(
                response.results.map {
                    Place(
                        id = it.id.id,
                        name = if (it.name.any()) it.name.first().value else "",
                        description = if (it.shortDescription.any()) it.shortDescription.first().value else "",
                        imageUrl = it.imageUrl
                    )
                }
            )
        } catch (ex: Exception) {
            return Result.Error(ex)
        }
    }
}