package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Location(
    val address: String?,
    val admin_region: String?,
    val country: String?,
    val formatted_address: String?,
    val locality: String?,
    val neighborhood: List<String>?,
    val postcode: String?,
    val region: String?
): java.io.Serializable