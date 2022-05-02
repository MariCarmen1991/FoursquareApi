package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Amenities(
    val live_music: Boolean?,
    val outdoor_seating: Boolean?,
    val wheelchair_accessible: Boolean?
): Serializable