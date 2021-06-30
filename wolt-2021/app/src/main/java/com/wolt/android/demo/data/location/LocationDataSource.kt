package com.wolt.android.demo.data.location

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ILocationDataSource {
    val location: Flow<Location>
}

class MockLocationDataSource(
    private val refreshIntervalMs: Long = 10000
) : ILocationDataSource {
    override val location: Flow<Location> = flow {
        while (true) {
            mockLocations.forEach {
                emit(it)
                delay(refreshIntervalMs)
            }
        }
    }
}

private val mockLocations = listOf(
    Location(60.170187, 24.930599),
    Location(60.169418, 24.931618),
    Location(60.169818, 24.932906),
    Location(60.170005, 24.935105),
    Location(60.169108, 24.936210),
    Location(60.168355, 24.934869),
    Location(60.167560, 24.932562),
    Location(60.168254, 24.931532),
    Location(60.169012, 24.930341),
    Location(60.170085, 24.929569)
)