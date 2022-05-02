package com.example.foursquareapiplaces


import android.Manifest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.provider.VoicemailContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.foursquareapiplaces.databinding.ActivityMainBinding
import com.example.foursquareapiplaces.ui.viewModel.VenuesViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.Places.initialize
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener



import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bundle: Bundle
    private lateinit var geo: String
    private lateinit var changueViewButton: ImageButton
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var searchItem: MenuItem



    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


                //---Init VARS--//
        val view = binding.root
        bundle = Bundle()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        changueViewButton = binding.changeView
        toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.options_menu)

        supportActionBar
        searchItem = toolbar.menu.findItem(R.id.search)
        setContentView(binding.root)


        //DEFAULT
        bundle.putString("texto", "")
        bundle.putString("ll","")



        var fragmentContainerView = findViewById<FragmentContainerView>(R.id.fragmentContainerView)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailsFragment) {

                toolbar.setVisibility(View.GONE)
                changueViewButton.visibility = View.GONE

                fragmentContainerView.layoutParams.height = 0
                fragmentContainerView.layoutParams.width = 0

            } else {
                toolbar.setVisibility(View.VISIBLE)
                changueViewButton.visibility = View.VISIBLE


            }
        }

        //METHODS//
        changueView()
        searchManage()
        places()
        requestPermissions()
        chargueMenu()

    }

    fun chargueMenu() {

        //--------Toolbar Items-------------------

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.favs->{

                    navHostFragment.findNavController().navigate(R.id.favsFragment)

                }

            }
            true



        }
    }






    @RequiresApi(Build.VERSION_CODES.M)
    fun changueView() {
        var isListView = true

        changueViewButton.setOnClickListener {
        requestPermissions()
            if (isListView) {
                isListView = false
                navHostFragment.findNavController().navigate(R.id.mapsFragment, bundle)
                changueViewButton.setImageResource(R.drawable.ic_baseline_map_24)
            } else {
                isListView = true

                navController.navigate(R.id.listFragment, bundle)
                changueViewButton.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24)
            }

        }


    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                            requestNewLocationData()
                    } else {

                        geo = "${location.latitude},${location.longitude}"
                        bundle.putString("geo", geo)
                        Log.d("MariCarmen", "Geolocalización coordenadas :" +location.accuracy+location.bearing)

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun searchManage() {

            val searchView = searchItem.actionView as SearchView
            searchView.setIconifiedByDefault(true);


            searchView.setOnSearchClickListener {
                //have permissions?
                requestPermissions()
            }


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("MariCarmen", "QUERY"+query)


                    //Pass the user search to fragments
                    bundle.putString("busqueda", query)

                    if (navController.currentDestination?.label == "fragment_list") {

                        navHostFragment.findNavController().navigate(R.id.listFragment, bundle)

                    } else {
                        navHostFragment.findNavController().navigate(R.id.mapsFragment, bundle)

                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.d("MariCarmen", "Place:"+place.latLng)

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    Log.i("MariCarmen", "error")
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("MariCarmen", status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    Log.i("MariCarmen", "CANCEL")
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun places(){

        //PLACES//
        val places = initialize(applicationContext,"AIzaSyBiDHWhYuAUAtGHXEDfkau6BoIoXgvPFTA" )
        val placesClient : PlacesClient= Places.createClient(this)
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
            LatLng(41.6988,2.84), LatLng(41.6988,2.84)
        ))


        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {



            override fun onError(p0: Status) {
                Log.d("MariCarmen", "An error occurred: $p0")
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPlaceSelected(p0: Place) {

                requestPermissions()
                Log.d("MariCarmen", "GEoPlace:"+p0.latLng.latitude)

                val geoPlace="${p0.latLng.latitude},${p0.latLng.longitude}"
                bundle.putString("place",geoPlace)

                Log.d("MariCarmen", "GEoPlace:"+geoPlace)

                if (navController.currentDestination?.label == "fragment_list") {
                    Log.d("MariCarmen", "listfragment")
                    navHostFragment.findNavController().navigate(R.id.listFragment, bundle)

                } else {
                    Log.d("MariCarmen", "mapsfragment")
                    navHostFragment.findNavController().navigate(R.id.mapsFragment, bundle)

                }
            }
        })

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1

        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            geo = "${mLastLocation.latitude},${mLastLocation.longitude}"
            Log.d("MariCarmen", "Fuseeed location-" + geo)
            bundle.putString("geo", geo)
        }
    }



    // - PERMISSIONS

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    getLastLocation()

                } else {


                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()


                }
                return
            }
        }

    }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun requestPermissions() {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog(getString(R.string.rationale_title),
                    getString(R.string.rationale_desc),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    MY_PERMISSIONS_REQUEST_LOCATION)

            }else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )


            }
        }


        @RequiresApi(Build.VERSION_CODES.M)
        private fun checkPermissions(): Boolean {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {

                return true

            } else {

                Log.d("Mari Carmen", "Permission for location is not granted")
                requestPermissions()
            }

            return true
        }




        //Rational Dialog

        @RequiresApi(Build.VERSION_CODES.M)
        private fun showRationaleDialog(
            title: String,
            message: String,
            permission: String,
            requestCode: Int,
        ) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", { dialog, which ->
                    requestPermissions(arrayOf(permission), requestCode)
                })
            builder.create().show()
        }


}

