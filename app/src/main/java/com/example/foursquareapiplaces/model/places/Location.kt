package com.example.foursquareapiplaces.model.places

data class Location(
    val address: String,
    val admin_region: String,
    val country: String,
    val cross_street: String,
    val formatted_address: String,
    val locality: String,
    val postcode: String,
    val region: String
)