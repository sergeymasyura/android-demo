package com.wolt.android.demo

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wolt.android.demo.data.PlacesRepository
import com.wolt.android.demo.data.favorite.FavoritesDataSource
import com.wolt.android.demo.data.location.MockLocationDataSource
import com.wolt.android.demo.data.search.SearchDataSource

/**
 * A Service Locator for the [PlacesRepository].
 */
object ServiceLocator {

    @Volatile
    var placesRepository: PlacesRepository? = null
        @VisibleForTesting set

    fun providePlacesRepository(context: Context): PlacesRepository {
        synchronized(this) {
            return placesRepository ?: placesRepository ?: createPlacesRepository(context)
        }
    }

    private fun createPlacesRepository(context: Context): PlacesRepository {
        val repository = PlacesRepository(
            searchDataSource = SearchDataSource("https://restaurant-api.wolt.fi/v3/"),
            favoritesDataSource = FavoritesDataSource(context.applicationContext),
            locationDataSource = MockLocationDataSource(refreshIntervalMs = 10000),
            placesLimit = 15
        )
        placesRepository = repository
        return repository
    }
}
