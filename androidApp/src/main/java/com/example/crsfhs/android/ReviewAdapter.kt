package com.example.crsfhs.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.api.HairdresserDetails
import com.example.crsfhs.android.api.Review

class ReviewAdapter(
    private val reviewList: ArrayList<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(reviewDetails: Review) {
            val image = itemView.findViewById<ImageView>(R.id.hairdresserIcon)
            val name = itemView.findViewById<TextView>(R.id.hairdresserName)
            val rating = itemView.findViewById<TextView>(R.id.hairdresserRating)
            val date = itemView.findViewById<TextView>(R.id.reviewDate)
            val description = itemView.findViewById<TextView>(R.id.reviewDescription)

            image.load(reviewDetails.hairdresserDetails.img.icon)

            name.text = reviewDetails.hairdresserDetails.name
            rating.text = reviewDetails.reviewDetails.rating
            date.text = reviewDetails.reviewDetails.date
            description.text = reviewDetails.reviewDetails.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = reviewList[position]
        holder.bindData(reviewItem)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}
