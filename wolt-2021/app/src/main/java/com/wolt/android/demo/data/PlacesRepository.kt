package com.wolt.android.demo.data

import com.wolt.android.demo.data.favorite.IFavoritesDataSource
import com.wolt.android.demo.data.location.ILocationDataSource
import com.wolt.android.demo.data.location.Location
import com.wolt.android.demo.data.search.ISearchDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

interface IPlacesRepository {
    val places: Flow<Result<List<Place>>>

    suspend fun toggleFavorite(place: Place): Result<Place>
}

class PlacesRepository(
    private val searchDataSource: ISearchDataSource,
    private val favoritesDataSource: IFavoritesDataSource,
    locationDataSource: ILocationDataSource,
    private val placesLimit: Int = 15
) : IPlacesRepository {
    override val places: Flow<Result<List<Place>>> =
        locationDataSource.location.mapLatest { location: Location ->
            val result = searchDataSource.search(location)

            when (result) {
                is Result.Success -> {
                    Result.Success(
                        result.data
                            .take(placesLimit)
                            .onEach { place ->
                                val favoriteResult = favoritesDataSource.isFavorite(place)
                                when (favoriteResult) {
                                    is Result.Success -> {
                                        place.favorite = favoriteResult.data
                                    }
                                    is Result.Error -> {
                                        return@mapLatest favoriteResult
                                    }
                                }
                            }
                    )
                }
                is Result.Error -> {
                    result
                }
            }
        }

    override suspend fun toggleFavorite(place: Place): Result<Place> {
        return try {
            return favoritesDataSource.setFavorite(place, !place.favorite)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}