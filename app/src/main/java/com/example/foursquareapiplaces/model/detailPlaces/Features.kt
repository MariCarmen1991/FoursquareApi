package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable

data class Features(
    val amenities: Amenities?,
    val food_and_drink: FoodAndDrink?,
    val payment: Payment?,
    val services: Services?
): java.io.Serializable