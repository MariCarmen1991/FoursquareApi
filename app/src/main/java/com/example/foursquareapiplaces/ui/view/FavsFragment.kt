package com.example.foursquareapiplaces.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapiplaces.FoursquareApiPlaces
import com.example.foursquareapiplaces.R
import com.example.foursquareapiplaces.databinding.FragmentFavsBinding
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.ui.adapters.PlaceAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavsFragment : Fragment(R.layout.fragment_favs) {

    private lateinit var placeAdapter:PlaceAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var binding: FragmentFavsBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favs, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentFavsBinding.bind(view)

        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        mRecyclerView=binding.favsRecyclerView

        var listFavorite= loadFavs()

        setRecyclerView(listFavorite)


    }


    fun loadFavs():ArrayList<DetailsPlaceResult>{

        var gson= Gson()
        var json= FoursquareApiPlaces.prefs.places

        var type= object: TypeToken<ArrayList<DetailsPlaceResult>>() {} .type

        val listFavPlaces=gson.fromJson<ArrayList<DetailsPlaceResult>>(json,type)

        return listFavPlaces
    }



    private fun setRecyclerView(places: ArrayList<DetailsPlaceResult>){
        placeAdapter= PlaceAdapter(places)

        mRecyclerView.apply {

            layoutManager= LinearLayoutManager(context)
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
                Log.d("Mari Carmen:", "Arraylistofdetails: ")


                val dialog = AlertDialog.Builder(requireActivity())
                    .setTitle("DELETE THIS PLACES")
                    .setMessage("Are you sure you want to delete this place")
                    .setNegativeButton("CANCEL") { view, _ ->
                        view.dismiss()
                    }
                    .setPositiveButton("YES") { view, _ ->

                        val list= loadFavs()

                        list.removeAt(position)
                        val gson=Gson()
                        var json=gson.toJson(list)
                        FoursquareApiPlaces.prefs.places=json
                        setRecyclerView(list)

                        navHostFragment.findNavController().navigate(R.id.favsFragment)

                        view.dismiss()
                    }
                    .setCancelable(false)
                    .create()

                dialog.show()

            }
        })



    }



}