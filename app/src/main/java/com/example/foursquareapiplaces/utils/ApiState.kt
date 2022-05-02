package com.example.foursquareapiplaces.utils

import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.model.places.FoursquareResponse

sealed class ApiState{

    object Loading: ApiState()

    class Failure(val message: Throwable): ApiState()

    class Succes(val data: FoursquareResponse): ApiState()

    object Empty: ApiState()

    //


    object LoadingDetails: ApiState()

    class FailureDetails(val message: Throwable): ApiState()

    class SuccesDetails(val detailPlaces: DetailsPlaceResult): ApiState()

    object EmptyDetails: ApiState()

    //








}
