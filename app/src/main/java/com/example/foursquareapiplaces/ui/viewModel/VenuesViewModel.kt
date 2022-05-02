package com.example.foursquareapiplaces.ui.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.foursquareapiplaces.utils.ApiState
import com.example.foursquareapiplaces.Network.ApiPlaces.VenuesRepository
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.model.places.FoursquareResponse
import com.example.foursquareapiplaces.model.places.Main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenuesViewModel @Inject constructor( val venuesRepository: VenuesRepository
): ViewModel() {

    private val stateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _stateFlow: StateFlow<ApiState> = stateFlow
    private val stateFlowDetails: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.EmptyDetails)
    private val stateFlowAuto: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.EmptyDetails)
    val _stateFlowAuto: StateFlow<ApiState> = stateFlowAuto

    val _stateFlowDetails: StateFlow<ApiState> = stateFlowDetails
    val listOfMarkers= MutableLiveData<Main>()
    var listOfPlaces=MutableLiveData<ArrayList<DetailsPlaceResult>>()
    var _listOfPlaces=ArrayList<DetailsPlaceResult>()
    var photo=MutableLiveData<ArrayList<String>>()

    private val venuesResult = MutableLiveData<FoursquareResponse>()


    //-- GET API RESPONSE : PLACES

    fun getSearchResults(ll:String, searchVenue: String) {
        viewModelScope.launch {


            venuesRepository.getThePlaces(ll,searchVenue)
                .onStart {stateFlow.value = ApiState.Loading  }
                .catch { e->
                    stateFlow.value = ApiState.Failure(e)

                    Log.d("Mari Carmen", "error "+e.message + ".."+e.localizedMessage+"..."+e.stackTraceToString())


                }
                .collect { data ->

                    if(data.results.size>0){

                        stateFlow.value = ApiState.Succes(data)
                        venuesResult.value = data

                        for(results in data.results){
                            getDetailsPlaces(results.fsq_id)

                        }

                        Log.d("Mari Carmen", "Viewmodel Response -->: " + data.results)

                    }else {

                        Log.d("Mari Carmen", "Empty: " )
                        stateFlow.value = ApiState.Empty


                    }

                }
        }
    }


    //-- GET API RESPONSE : DETAILS

    fun getDetailsPlaces(id:String){


            viewModelScope.launch {
                stateFlowDetails.value = ApiState.LoadingDetails

                venuesRepository.getDetailPlaces(id).catch {

                        e-> ApiState.FailureDetails(e)

                            Log.d("Mari Carmen", "Details Error "+e.stackTraceToString())


                }.collect {

                        detailPlaces ->  stateFlowDetails.value = ApiState.SuccesDetails(detailPlaces)
                    Log.d("Mari Carmen", "DETAILS OF PLACES "+detailPlaces.copy() )


                    _listOfPlaces.add(detailPlaces)
                    }

                setPlaces(_listOfPlaces)





            }



    }



    fun setPlaces(listOfDetailsPlaces: ArrayList<DetailsPlaceResult>){

        listOfPlaces.value= _listOfPlaces

        Log.d("MariCarmen", "dentro de getplaces ->"+listOfPlaces.value.toString())
    }



}