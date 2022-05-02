package com.example.foursquareapiplaces

import android.app.Application
import com.example.foursquareapiplaces.utils.DataFavsRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoursquareApiPlaces :Application(){



    companion object {
        lateinit var prefs: DataFavsRepository
    }
    override fun onCreate() {
        super.onCreate()
        prefs = DataFavsRepository(applicationContext)
    }

}