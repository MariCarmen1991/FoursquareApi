package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Photo(
    val classifications: List<String>?,
    val created_at: String?,
    val height: Int?,
    val id: String?,
    val prefix: String?,
    val suffix: String?,
    val width: Int?
): Serializable