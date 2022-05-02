package com.example.foursquareapiplaces.ui.view

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapiplaces.ApiState
import com.example.foursquareapiplaces.R
import com.example.foursquareapiplaces.databinding.FragmentMapsBinding
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.model.places.Result
import com.example.foursquareapiplaces.ui.adapters.PlaceAdapter
import com.example.foursquareapiplaces.ui.viewModel.VenuesViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {


    private val venuesViewModel by viewModels<VenuesViewModel>()
    private var map : GoogleMap? = null
    private lateinit var listOfMarkes: ArrayList<LatLng>
    private lateinit var listofPlaces: ArrayList<Result>
    private lateinit var binding : FragmentMapsBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map=googleMap

        if(!arguments?.isEmpty!!){

            var texto= arguments?.getString("busqueda")
            var place=arguments?.getString("place")
            Log.d("Mari Carmen:", "variables"+texto+"  "+place)

            if(texto.isNullOrEmpty()&&place.isNullOrEmpty()){
                Log.d("MariCarmen", "geocodes-----inside--------------"+ arguments?.getString("geo"))
                val geo = arguments?.getString("geo")!!.toString()
                val split = geo.split(",")

                val lat = split.get(0).toDouble()
                val long = split.get(1).toDouble()
                val yourposition = LatLng(lat, long)
                googleMap.addMarker(MarkerOptions().position(yourposition).title("You are here").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourposition, 15F))

                venuesViewModel.getSearchResults(arguments?.getString("geo")!!," ")

            }
            else if(texto==null){
                texto=""

                Log.d("Mari Carmen:", "texto null")
                venuesViewModel.getSearchResults(place!!, texto)

            }
            else if(place==null){
                Log.d("Mari Carmen:", "place null")
                place=""
                venuesViewModel.getSearchResults(place, texto)

            }else{
                venuesViewModel.getSearchResults(place, texto)

            }

        }else{

            Log.d("Mari Carmen:", "NOT FOUND")
        }



        venuesViewModel.listOfPlaces.observe(viewLifecycleOwner, Observer {

            Log.d("MariCarmen", "resulmapstadooooooooooooo " + it[0])

            if(it.size>0){
                Log.d("MariCarmen", "resulmapstadooooooooooooo " + it[0].geocodes!!.main?.latitude)

                //SET PLACES ON THE MAP AND INFLATE RECYCLERVIEW
                feedMarkers(it)
                setRecyclerView(it)


            }else {

                Toast.makeText(activity,"Do a search or find it in another way", Toast.LENGTH_LONG).show()
                Log.d("MariCarmen", "empty")

            }
        })

        lifecycleScope.launchWhenCreated {
            venuesViewModel._stateFlow.collect{
                when(it){
                    ApiState.Empty->{
                        Toast.makeText(activity,"Nothing found. Try another search", Toast.LENGTH_LONG).show()
                        Log.d("MariCarmen", "empty")
                    }
                }
            }
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val rootView=FragmentMapsBinding.inflate(layoutInflater)
       // val rootView= inflater.inflate(R.layout.fragment_maps, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        return rootView.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding= FragmentMapsBinding.bind(view)
        navHostFragment = activity?.supportFragmentManager!!.
        findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        mRecyclerView=binding.recyclermaps

        listOfMarkes=ArrayList<LatLng>()
    }


    fun feedMarkers(list:ArrayList<DetailsPlaceResult> ){


        //inflate markers with geo
        var position: Int
        for(result in list){

            var latLng =LatLng(result.geocodes?.main!!.latitude!!, result.geocodes.main.longitude!!)
            map?.addMarker(MarkerOptions().position(latLng).title(result.name))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            listOfMarkes.add(latLng)

        }



        map?.setOnMarkerClickListener {
                marker ->

           position= list.indexOf(list.find { it.name==marker.title })

            mRecyclerView.scrollToPosition(position)

            false
        }



    }

    private fun setRecyclerView(places: ArrayList<DetailsPlaceResult>){
        placeAdapter= PlaceAdapter(places)

        mRecyclerView.apply {

            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        mRecyclerView.adapter=placeAdapter

        placeAdapter.setOnItemClickListener(object : PlaceAdapter.OnClickListener{
            override fun onItemClick(position: Int) {

                val bundle=Bundle()
                bundle.putInt("position",position)
                bundle.putSerializable("searchItem",places[position])
                Log.d("Mari Carmen:", "Arraylistofdetails: "+places[0])

                navHostFragment.findNavController().navigate(R.id.detailsFragment,bundle)




            }

            override fun onLongClick(position: Int) {
                TODO("Not yet implemented")
            }
        })



    }

}