package com.example.foursquareapiplaces.Network.ApiPlaces

import com.example.foursquareapiplaces.REQUEST_FIELDS
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult

import com.example.foursquareapiplaces.model.places.FoursquareResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiPlaces {

    // Call venues

    @GET("places/search?radius=3000")
    suspend fun requestVenues(

                      @Query(value="ll") ll: String,
                      @Query(value="query") query: String
    ): Response<FoursquareResponse>


    @GET("places/{fsq_id}?")
    suspend fun requestVenueDetails(@Path(value="fsq_id") fsq_id: String,
                         @Query(value="fields") fields: String = REQUEST_FIELDS,

    ): Response<DetailsPlaceResult>



}