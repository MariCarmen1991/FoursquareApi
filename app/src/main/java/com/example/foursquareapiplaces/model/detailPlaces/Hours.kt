package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Hours(
    val display: String?,
    val is_local_holiday: Boolean?,
    val open_now: Boolean?,
    val seasonal: List<Any>?
): java.io.Serializable