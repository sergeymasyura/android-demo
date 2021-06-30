package com.wolt.android.demo.data

import com.wolt.android.demo.data.favorite.IFavoritesDataSource
import com.wolt.android.demo.data.location.ILocationDataSource
import com.wolt.android.demo.data.location.Location
import com.wolt.android.demo.data.search.ISearchDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import java.lang.Exception

class PlacesRepositoryUnitTest {

    @Test
    fun places_success() = runBlocking {
        val latitude = 60.170187
        val longitude = 24.930599

        val searchDataSource = object : ISearchDataSource {
            override suspend fun search(location: Location): Result<List<Place>> {
                assertThat(location.latitude, `is`(latitude))
                assertThat(location.longitude, `is`(longitude))
                return Result.Success(
                    listOf(
                        Place(
                            id = "1",
                            name = "place 1",
                            description = "description 1",
                            imageUrl = "url 1"
                        )
                    )
                )
            }
        }

        val favoritesDataSource = object : IFavoritesDataSource {
            override suspend fun isFavorite(place: Place): Result<Boolean> {
                return Result.Success(false)
            }

            override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
                error("not implemented")
            }
        }

        val locationDataSource = object : ILocationDataSource {
            override val location: Flow<Location>
                get() = flowOf(Location(latitude, longitude))
        }

        val repository = PlacesRepository(
            searchDataSource,
            favoritesDataSource,
            locationDataSource
        )

        val result = repository.places.first()
        assertThat(result, IsInstanceOf(Result.Success::class.java))
        val places = (result as Result.Success).data

        assertThat(places.count(), `is`(1))
        assertThat(places[0].id, `is`("1"))
        assertThat(places[0].name, `is`("place 1"))
        assertThat(places[0].description, `is`("description 1"))
        assertThat(places[0].imageUrl, `is`("url 1"))
        assertThat(places[0].favorite, `is`(false))
    }

    @Test
    fun places_success_favorite_true() = runBlocking {
        val searchDataSource = object : ISearchDataSource {
            override suspend fun search(location: Location): Result<List<Place>> {
                return Result.Success(
                    listOf(
                        Place(
                            id = "1",
                            name = "place 1",
                            description = "description 1",
                            imageUrl = "url 1"
                        )
                    )
                )
            }
        }

        val favoritesDataSource = object : IFavoritesDataSource {
            override suspend fun isFavorite(place: Place): Result<Boolean> {
                return Result.Success(true)
            }

            override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
                error("not implemented")
            }
        }

        val locationDataSource = object : ILocationDataSource {
            override val location: Flow<Location>
                get() = flowOf(Location(60.170187, 24.930599))
        }

        val repository = PlacesRepository(
            searchDataSource,
            favoritesDataSource,
            locationDataSource
        )

        val result = repository.places.first()
        assertThat(result, IsInstanceOf(Result.Success::class.java))
        val places = (result as Result.Success).data

        assertThat(places.count(), `is`(1))
        assertThat(places[0].id, `is`("1"))
        assertThat(places[0].name, `is`("place 1"))
        assertThat(places[0].description, `is`("description 1"))
        assertThat(places[0].imageUrl, `is`("url 1"))
        assertThat(places[0].favorite, `is`(true))
    }

    @Test
    fun places_favorite_error() = runBlocking {
        val searchDataSource = object : ISearchDataSource {
            override suspend fun search(location: Location): Result<List<Place>> {
                return Result.Success(
                    listOf(
                        Place(
                            id = "1",
                            name = "place 1",
                            description = "description 1",
                            imageUrl = "url 1"
                        )
                    )
                )
            }
        }

        val favoritesDataSource = object : IFavoritesDataSource {
            override suspend fun isFavorite(place: Place): Result<Boolean> {
                return Result.Error(Exception("Failed to set favorite"))
            }

            override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
                error("not implemented")
            }
        }

        val locationDataSource = object : ILocationDataSource {
            override val location: Flow<Location>
                get() = flowOf(Location(60.170187, 24.930599))
        }

        val repository = PlacesRepository(
            searchDataSource,
            favoritesDataSource,
            locationDataSource
        )

        val result = repository.places.first()
        assertThat(result, IsInstanceOf(Result.Error::class.java))
    }

    @Test
    fun places_search_error() = runBlocking {
        val searchDataSource = object : ISearchDataSource {
            override suspend fun search(location: Location): Result<List<Place>> {
                return Result.Error(
                    Exception("Failed to search places")
                )
            }
        }

        val favoritesDataSource = object : IFavoritesDataSource {
            override suspend fun isFavorite(place: Place): Result<Boolean> {
                return Result.Success(false)
            }

            override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
                error("not implemented")
            }
        }

        val locationDataSource = object : ILocationDataSource {
            override val location: Flow<Location>
                get() = flowOf(Location(60.170187, 24.930599))
        }

        val repository = PlacesRepository(
            searchDataSource,
            favoritesDataSource,
            locationDataSource
        )

        val result = repository.places.first()
        assertThat(result, IsInstanceOf(Result.Error::class.java))
    }

    @Test
    fun places_limit() = runBlocking {
        val searchDataSource = object : ISearchDataSource {
            override suspend fun search(location: Location): Result<List<Place>> {
                return Result.Success(
                    listOf(
                        Place(
                            id = "1",
                            name = "place 1",
                            description = "description 1",
                            imageUrl = "url 1"
                        ),
                        Place(
                            id = "2",
                            name = "place 2",
                            description = "description 2",
                            imageUrl = "url 2"
                        )
                    )
                )
            }
        }

        val favoritesDataSource = object : IFavoritesDataSource {
            override suspend fun isFavorite(place: Place): Result<Boolean> {
                return Result.Success(false)
            }

            override suspend fun setFavorite(place: Place, isFavorite: Boolean): Result<Place> {
                error("not implemented")
            }
        }

        val locationDataSource = object : ILocationDataSource {
            override val location: Flow<Location>
                get() = flowOf(Location(60.170187, 24.930599))
        }

        val repository = PlacesRepository(
            searchDataSource,
            favoritesDataSource,
            locationDataSource,
            placesLimit = 1
        )

        val result = repository.places.first()
        assertThat(result, IsInstanceOf(Result.Success::class.java))
        val places = (result as Result.Success).data

        assertThat(places.count(), `is`(1))
        assertThat(places[0].id, `is`("1"))
        assertThat(places[0].name, `is`("place 1"))
        assertThat(places[0].description, `is`("description 1"))
        assertThat(places[0].imageUrl, `is`("url 1"))
        assertThat(places[0].favorite, `is`(false))
    }
}