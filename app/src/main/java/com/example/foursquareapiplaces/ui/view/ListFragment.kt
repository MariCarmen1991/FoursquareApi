package com.example.foursquareapiplaces.ui.view



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapiplaces.utils.ApiState
import com.example.foursquareapiplaces.R
import com.example.foursquareapiplaces.databinding.FragmentListBinding
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.example.foursquareapiplaces.ui.adapters.PlaceAdapter
import com.example.foursquareapiplaces.ui.viewModel.VenuesViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlin.collections.ArrayList

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val venuesViewModel by viewModels<VenuesViewModel>()
    private lateinit var placeAdapter:PlaceAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var navHostFragment: NavHostFragment


    //Binding
    private lateinit var binding : FragmentListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setHasOptionsMenu(true)

    }



        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            val view = inflater.inflate(R.layout.fragment_list, container, false)

            return view

        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding= FragmentListBinding.bind(view)

            navHostFragment = activity?.supportFragmentManager!!.
            findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            mRecyclerView=binding.placesRecyclerView


            if(!arguments?.isEmpty!!){

                var texto= arguments?.getString("busqueda")
                var place=arguments?.getString("place")
                Log.d("Mari Carmen:", "variables"+texto+"  "+place)

                if(texto==null){
                    texto=""
                    Log.d("Mari Carmen:", "texto null")

                }
                else if(place==null){
                    Log.d("Mari Carmen:", "place null")
                    place=""
                }else{
                    venuesViewModel.getSearchResults(place, texto)
                    Log.d("Mari Carmen:", "buscar")

                }
                venuesViewModel.getSearchResults(place!!, texto)

            }


                venuesViewModel.listOfPlaces.observe(viewLifecycleOwner, Observer {
                    Log.d("Mari Carmen:", "Arraylistofdetails: "+it[0].name)

                    if( it.size>0){
                        setRecyclerView(it)

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


    //RECYCLERVIEW METHODS

    private fun setRecyclerView(places: ArrayList<DetailsPlaceResult>){
        placeAdapter= PlaceAdapter(places)

        mRecyclerView.apply {

            layoutManager=LinearLayoutManager(context)
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

            }

        })

    }




}