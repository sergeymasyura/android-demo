package com.wolt.android.demo

import android.app.Application
import com.wolt.android.demo.data.PlacesRepository

class WoltApplication : Application() {

    val placesRepository: PlacesRepository
        get() = ServiceLocator.providePlacesRepository(this)
}