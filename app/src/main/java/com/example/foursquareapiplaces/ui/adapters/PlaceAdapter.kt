package com.example.foursquareapiplaces.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapiplaces.R
import com.example.foursquareapiplaces.model.detailPlaces.DetailsPlaceResult
import com.squareup.picasso.Picasso

class PlaceAdapter( private var placesList: List<DetailsPlaceResult> ): RecyclerView.Adapter<PlaceAdapter.MyViewHolder>() {

    private lateinit var mListener: OnClickListener



    interface OnClickListener {
        fun onItemClick(position: Int)
        fun onLongClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnClickListener) {

        mListener = listener

    }

    fun setOnLongClickListener(listener: OnClickListener):Boolean{
        mListener = listener

        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_search_layout, parent, false)


        return MyViewHolder(v, mListener)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = placesList[position]

        holder.placeName.text = currentItem.name

        for(categories in currentItem.categories!!){
            holder.placeCategory.text =categories.name

            holder.iconPlace.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)

            Picasso.get()
                .load("${categories.icon?.prefix}32${categories.icon?.suffix}")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.iconPlace)

        }

        holder.address.text= currentItem.location!!.formatted_address
        holder.score.text= currentItem.rating.toString()
        if(currentItem.rating !=null) {

            if (currentItem.rating!! > 0.0 &&  currentItem.rating < 4.0) {
                holder.score.setTextColor(Color.parseColor("#E6092C"))

            } else {
                if (currentItem.rating > 4.0 && currentItem.rating <= 5.0) {
                    holder.score.setTextColor(Color.parseColor("#FFC800"))


                } else
                    if (currentItem.rating!! > 5.0 && currentItem.rating!! <= 6.0) {
                    holder.score.setTextColor(R.color.mOrange)
                        holder.score.setTextColor(Color.parseColor("#FF9600"))

                } else
                    if (currentItem.rating!! > 6.0 && currentItem.rating!! <= 7.0) {

                    holder.score.setTextColor(Color.parseColor("#FFC800"))


                } else
                    if (currentItem.rating!! > 7.0 && currentItem.rating!! <= 8.0) {
                        holder.score.setTextColor(Color.parseColor("#C5DE35"))


                } else
                    if (currentItem.rating!! > 8 && currentItem.rating!! <= 9) {
                        holder.score.setTextColor(Color.parseColor("#73CF42"))


                } else {
                        holder.score.setTextColor(Color.parseColor("#00B551"))

                }
            }
        }else{
            holder.score.text="N/S"

        }


//corregir error de índices

        if(!currentItem.photos.isNullOrEmpty()) {
            Picasso.get()
                .load("${currentItem.photos!![0].prefix}original${currentItem.photos[0].suffix}")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imagePlace)
        }

    }

    override fun getItemCount(): Int = placesList.size

    class MyViewHolder(itemView: View, listener: OnClickListener ) :
        RecyclerView.ViewHolder(itemView) {
        var placeName = itemView.findViewById<TextView>(R.id.tw_name_item)
        var placeCategory = itemView.findViewById<TextView>(R.id.category_tw)
        var address = itemView.findViewById<TextView>(R.id.tw_address)
        var imagePlace= itemView.findViewById<ImageView>(R.id.iv_place_image)
        var iconPlace= itemView.findViewById<ImageView>(R.id.iconPlace)
        var score=itemView.findViewById<TextView>(R.id.score_tv)
        var distance=itemView.findViewById<TextView>(R.id.tw_distance)
        var website=itemView.findViewById<TextView>(R.id.web_text_id)
        //var cardView=itemView.findViewById<CardView>(R.id.cardView_score)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)


            }


        }

        init {
            itemView.setOnLongClickListener(){
                listener.onLongClick(bindingAdapterPosition)

                true
            }

        }

    }

}
