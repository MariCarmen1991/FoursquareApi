package com.example.foursquareapiplaces

import android.content.Context
import android.content.SharedPreferences


class DataFavsRepository (context: Context) {


    val PREFS_NAME = "com.cursokotlin.sharedpreferences"
    val SHARED_NAME = "PLACES"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
    var places: String
        get() = prefs.getString(SHARED_NAME, "")!!

        set(value) = prefs.edit().putString(SHARED_NAME, value).apply()
}