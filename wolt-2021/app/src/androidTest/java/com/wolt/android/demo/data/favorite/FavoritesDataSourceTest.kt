package com.wolt.android.demo.data.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wolt.android.demo.data.Place
import com.wolt.android.demo.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavoritesDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun set_and_get_favorite() = runBlocking {

        val favoritesDataSource = FavoritesDataSource(getApplicationContext())

        val place = Place(
            id = "1",
            name = "place 1",
            description = "description 1",
            imageUrl = "url 1"
        )

        favoritesDataSource.setFavorite(place, isFavorite = true)
        var result = favoritesDataSource.isFavorite(place)
        assertThat(result, IsInstanceOf(Result.Success::class.java))
        var isFavorite = (result as Result.Success).data
        assertThat(isFavorite, `is`(true))

        favoritesDataSource.setFavorite(place, isFavorite = false)
        result = favoritesDataSource.isFavorite(place)
        isFavorite = (result as Result.Success).data
        assertThat(isFavorite, `is`(false))
    }
}
