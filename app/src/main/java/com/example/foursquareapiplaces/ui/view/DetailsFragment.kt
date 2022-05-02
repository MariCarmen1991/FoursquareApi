package com.example.foursquareapiplaces.ui.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.foursquareapiplaces.FoursquareApiPlaces
import com.example.foursquareapiplaces.GOOGLE_API
import com.example.foursquareapiplaces.R
import com.example.foursquareapiplaces.databinding.FragmentDetailsBinding
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.ui.viewModel.VenuesViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding : FragmentDetailsBinding
    val viewModelDetail by viewModels<VenuesViewModel>()
    private  var listOfPlaces =ArrayList<DetailsPlaceResult>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MariCarmen", "DETAILS")



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view=inflater.inflate(R.layout.fragment_details, container, false)

        /*viewModelDetail.listOfPlaces.observe(viewLifecycleOwner, Observer {
            Log.d("MariCarmen", "DETAILS"+it[0].name)

        })*/

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentDetailsBinding.bind(view)

        var position = arguments?.getInt("position")
        val place=arguments?.getSerializable("searchItem") as DetailsPlaceResult
        Log.d("MariCarmen", "..."+place)

        var progressPrice=binding.progressBarPrice

                 if(place.price !=null)   {
                     if(place.price ==1){
                         progressPrice.setProgress(place.price,true)
                         progressPrice.progressTintList = ColorStateList.valueOf(Color.GREEN)
                     }
                     else if(place.price ==2){
                         progressPrice.setProgress(place.price,true)

                         progressPrice.progressTintList = ColorStateList.valueOf(Color.MAGENTA)

                     }
                     else if(place.price ==3){
                         progressPrice.setProgress(place.price,true)
                         progressPrice.progressTintList = ColorStateList.valueOf(Color.RED)


                     }else{

                     }
                 }


                binding.adressTv.text = place.location?.formatted_address
                binding.categoriesTv.text= place.categories!![0].name
                binding.namePlaceId.text = place.name
                binding.phoneTextId.text=place.tel
                binding.webTextId.text=place.website
                binding.progressBar.setProgress(place.rating!!.toInt())
                binding.scoreDetailsTv.text=place.rating.toString()



        //Google static map

                val imageMap=binding.imageMapId
                val lat = place.geocodes?.main?.latitude.toString()
                val lon = place.geocodes?.main?.longitude.toString()

        Picasso.get()
            .load("https://maps.googleapis.com/maps/api/staticmap?center=${lat},${lon}&zoom=18&size=280x280&markers=color:red|23.006562499999994,72.50172265625&key="+GOOGLE_API)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher_round)
            .into(imageMap)


      //rating
        binding.ratingTexTv.text= place.stats?.total_ratings.toString()
                if (place.hours!!.open_now!!) {
                    binding.openId.text = "Open"
                    binding.openId.setTextColor(Color.parseColor("#C5DE35"))
                } else {
                    binding.openId.text = "Closed"
                    binding.openId.setTextColor(Color.parseColor("#E6092C"))

                }
        //photo

                if(!place.photos.isNullOrEmpty()) {
                    Picasso.get()
                        .load("${place.photos!![0].prefix}original${place.photos!![0].suffix}")
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher_round)
                        .into(binding.imagePlace)
                }

        saveFavs(place)

    }


    fun saveFavs(place:DetailsPlaceResult){
        val buttonAdd= binding.addPlaceId
       // listOfPlaces= loadFavs()
        listOfPlaces.add(place)

        buttonAdd.setOnClickListener {
            var gson=Gson()
            var json=gson.toJson(listOfPlaces)

            FoursquareApiPlaces.prefs.places=json

            Toast.makeText(activity, "Place Saved", Toast.LENGTH_LONG).show()

        }

    }

   /* fun loadFavs():ArrayList<DetailsPlaceResult>{

        var gson= Gson()
        var json= FoursquareApiPlaces.prefs.places
        var type= object: TypeToken<ArrayList<DetailsPlaceResult>>() {} .type
        val listFavPlaces=gson.fromJson<ArrayList<DetailsPlaceResult>>(json,type)



        return listFavPlaces
    }*/





}