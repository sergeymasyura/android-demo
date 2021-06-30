package com.wolt.android.demo


import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wolt.android.demo.data.IPlacesRepository
import com.wolt.android.demo.data.Place
import com.wolt.android.demo.data.Result
import com.wolt.android.demo.util.MainCoroutineRule
import com.wolt.android.demo.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test


/**
 * Unit tests for the implementation of [PlacesListViewModel]
 */
@ExperimentalCoroutinesApi
class PlacesListViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shows_items_on_success() {
        val repository = object : IPlacesRepository {
            override val places: Flow<Result<List<Place>>>
                get() {
                    return flowOf(
                        Result.Success(
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
                    )
                }

            override suspend fun toggleFavorite(place: Place): Result<Place> {
                error("not implemented")
            }
        }

        val placesListViewModel = PlacesListViewModel(repository)
        val placesEvent = placesListViewModel.onShowPlaces.getOrAwaitValue()
        val placesContent = placesEvent.getContentIfNotHandled()
        assertThat(placesContent, not(nullValue()))
        assertThat(placesContent!!.places.count(), `is`(2))
        assertThat(placesContent.places[0].id, `is`("1"))
        assertThat(placesContent.places[0].name, `is`("place 1"))
        assertThat(placesContent.places[0].description, `is`("description 1"))
        assertThat(placesContent.places[0].imageUrl, `is`("url 1"))
        assertThat(placesContent.places[1].id, `is`("2"))
        assertThat(placesContent.places[1].name, `is`("place 2"))
        assertThat(placesContent.places[1].description, `is`("description 2"))
        assertThat(placesContent.places[1].imageUrl, `is`("url 2"))
    }

    @Test
    fun shows_notification_on_empty_search() {
        val repository = object : IPlacesRepository {
            override val places: Flow<Result<List<Place>>>
                get() {
                    return flowOf(
                        Result.Success(
                            emptyList()
                        )
                    )
                }

            override suspend fun toggleFavorite(place: Place): Result<Place> {
                error("not implemented")
            }
        }

        val placesListViewModel = PlacesListViewModel(repository)

        val statusEvent = placesListViewModel.onShowStatus.getOrAwaitValue()
        val statusContent = statusEvent.getContentIfNotHandled()
        assertThat(statusContent, not(nullValue()))
        assertThat(statusContent!!.messageResId, `is`(R.string.notification_search_empty))
    }

    @Test
    fun shows_notification_on_error() {
        val repository = object : IPlacesRepository {
            override val places: Flow<Result<List<Place>>>
                get() {
                    return flowOf(
                        Result.Error(
                            NetworkErrorException()
                        )
                    )
                }

            override suspend fun toggleFavorite(place: Place): Result<Place> {
                error("not implemented")
            }
        }

        val placesListViewModel = PlacesListViewModel(repository)

        val statusEvent = placesListViewModel.onShowStatus.getOrAwaitValue()
        val statusContent = statusEvent.getContentIfNotHandled()
        assertThat(statusContent, not(nullValue()))
        assertThat(statusContent!!.messageResId, `is`(R.string.notification_search_failure))
    }

    @Test
    fun updates_item_on_toggle_favorite() {
        val place = Place(
            id = "1",
            name = "place 1",
            description = "description 1",
            imageUrl = "url 1"
        )

        val repository = object : IPlacesRepository {
            override val places: Flow<Result<List<Place>>>
                get() {
                    return flowOf(
                        Result.Success(
                            listOf(
                                place
                            )
                        )
                    )
                }

            override suspend fun toggleFavorite(place: Place): Result<Place> {
                return Result.Success(place)
            }
        }

        val placesListViewModel = PlacesListViewModel(repository)
        placesListViewModel.onFavoriteClick(place, position = 1)

        val updateItemEvent = placesListViewModel.onUpdateItem.getOrAwaitValue()
        val updateItemContent = updateItemEvent.getContentIfNotHandled()
        assertThat(updateItemContent, not(nullValue()))
        assertThat(updateItemContent!!.place.id, `is`(place.id))
        assertThat(updateItemContent.position, `is`(1))
    }

    @Test
    fun shows_toast_on_toggle_favorite_error() {
        val place = Place(
            id = "1",
            name = "place 1",
            description = "description 1",
            imageUrl = "url 1"
        )

        val repository = object : IPlacesRepository {
            override val places: Flow<Result<List<Place>>>
                get() {
                    return flowOf(
                        Result.Success(
                            listOf(
                                place
                            )
                        )
                    )
                }

            override suspend fun toggleFavorite(place: Place): Result<Place> {
                return Result.Error(
                    Exception()
                )
            }
        }

        val placesListViewModel = PlacesListViewModel(repository)
        placesListViewModel.onFavoriteClick(place, position = 1)

        val showToastEvent = placesListViewModel.onShowToast.getOrAwaitValue()
        val showToastContent = showToastEvent.getContentIfNotHandled()
        assertThat(showToastContent, not(nullValue()))
        assertThat(showToastContent!!.messageResId, `is`(R.string.notification_favorite_failure))
    }
}
