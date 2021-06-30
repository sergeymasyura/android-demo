package com.wolt.android.demo.data.location

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class LocationDataSourceTest {

    @Test
    fun location_repeats() = runBlocking {
        val locationDataSource = MockLocationDataSource(10)

        val locations = locationDataSource.location.take(20).toList()

        assertThat(locations.count(), `is`(20))
        assertThat(locations[0].latitude, `is`(60.170187))
        assertThat(locations[0].longitude, `is`(24.930599))
        assertThat(locations[9].latitude, `is`(60.170085))
        assertThat(locations[9].longitude, `is`(24.929569))
        assertThat(locations[10].latitude, `is`(60.170187))
        assertThat(locations[10].longitude, `is`(24.930599))
        assertThat(locations[19].latitude, `is`(60.170085))
        assertThat(locations[19].longitude, `is`(24.929569))
    }
}
