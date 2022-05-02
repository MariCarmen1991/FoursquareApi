package com.example.foursquareapiplaces.model.places

data class FoursquareResponse(
    val context: Context,
    val results: List<Result>
)