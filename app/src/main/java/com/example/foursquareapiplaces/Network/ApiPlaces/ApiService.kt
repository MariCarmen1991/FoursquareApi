package com.example.foursquareapiplaces.Network.ApiPlaces

import android.util.Log
import com.example.foursquareapiplaces.REQUEST_FIELDS
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.model.places.FoursquareResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ApiService @Inject constructor( private val api: ApiPlaces){


    suspend fun getPlacesResponse(ll:String, searchVenue: String): FoursquareResponse {
        return withContext(Dispatchers.IO) {
            val mResponse: Response<FoursquareResponse> = api.requestVenues(ll, searchVenue)

            if(mResponse.body()!=null){
                Log.d("MariCarmen", "Success")
            }
            else{
                Log.d("MariCarmen", "error "+mResponse.errorBody().toString())


            }
       mResponse.body()!!


        }

    }



    suspend fun getDetailPlacesResponse(id: String): DetailsPlaceResult {
        return withContext(Dispatchers.IO) {
            val resultDetails: Response<DetailsPlaceResult> = api.requestVenueDetails(id, REQUEST_FIELDS)
            resultDetails.body()!!
        }
    }









}