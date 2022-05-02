package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Stats(
    val total_photos: Int?,
    val total_ratings: Int?,
    val total_tips: Int?
): Serializable