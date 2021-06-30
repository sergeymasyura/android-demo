package com.wolt.android.demo.data.favorite

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.wolt.android.demo.data.Place
import com.wolt.android.demo.data.Result
import kotlinx.coroutines.flow.firstOrNull

interface IFavoritesDataSource {
    suspend fun isFavorite(place: Place): Result<Boolean>
    suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place>
}

class FavoritesDataSource(
    private val context: Context
) : IFavoritesDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

    override suspend fun isFavorite(place: Place): Result<Boolean> {
        val key = booleanPreferencesKey(place.id)
        val preferences: Preferences? = context.dataStore.data.firstOrNull()
        return Result.Success(preferences?.get(key) ?: false)
    }

    override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
        val key = booleanPreferencesKey(place.id)
        context.dataStore.edit {
            it[key] = isFavorite
        }
        place.favorite = isFavorite
        return Result.Success(place)
    }
}
