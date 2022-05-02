package com.example.foursquareapiplaces.Network.ApiPlaces

import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.model.places.FoursquareResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VenuesRepository @Inject constructor( private val apiResponse: ApiService) {

    //get

    fun getThePlaces(ll:String, searchVenue: String ): Flow<FoursquareResponse>{
        return flow { val venuesResult= apiResponse.getPlacesResponse(ll, searchVenue)
        emit(venuesResult)
        }.flowOn(Dispatchers.IO)
    }




    fun getDetailPlaces(id:String): Flow<DetailsPlaceResult>{
        return flow {val detailsPlaceResults= apiResponse.getDetailPlacesResponse(id)
            emit(detailsPlaceResults)
        }.flowOn(Dispatchers.IO)
    }


}