package com.example.crsfhs.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.HairdresserDetails

class FavoritesAdapter(
    private val hairdresserList: ArrayList<HairdresserDetails>
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(hairdresserDetails: HairdresserDetails) {
            val image = itemView.findViewById<ImageView>(R.id.hairdresserIcon)
            val name = itemView.findViewById<TextView>(R.id.hairdresserName)
            val address = itemView.findViewById<TextView>(R.id.hairdresserAddress)
            val rating = itemView.findViewById<TextView>(R.id.hairdresserRating)

            val hairdresserAddress =
                "${hairdresserDetails.address.street} " +
                        "${hairdresserDetails.address.number}, " +
                        "${hairdresserDetails.address.postcode} " +
                        hairdresserDetails.address.city

            image.load(hairdresserDetails.img.icon)

            name.text = hairdresserDetails.name
            address.text = hairdresserAddress
            rating.text = hairdresserDetails.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = hairdresserList[position]
        holder.bindData(favoriteItem)
        //holder.itemView.setOnClickListener { listener(favoriteItem) }
    }

    override fun getItemCount(): Int {
        return hairdresserList.size
    }
}
