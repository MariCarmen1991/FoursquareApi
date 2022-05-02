package com.example.foursquareapiplaces.model.detailPlaces

import java.io.Serializable


data class DetailsPlaceResult(
    val categories: List<Category>?,
    val chains: List<Any>?,
    val description: String?,
    val features: Features?,
    val fsq_id: String?,
    val geocodes: Geocodes?,
    val hours: Hours?,
    val hours_popular: List<HoursPopular>?,
    val location: Location?,
    val name: String?,
    val photos: List<Photo>?,
    val popularity: Double?,
    val price: Int?,
    val rating: Double?,
    val related_places: RelatedPlaces?,
    val social_media: SocialMedia?,
    val stats: Stats?,
    val tastes: List<String>?,
    val tel: String?,
    val timezone: String?,
    val tips: List<Tip>?,
    val verified: Boolean?,
    val website: String?
): Serializable