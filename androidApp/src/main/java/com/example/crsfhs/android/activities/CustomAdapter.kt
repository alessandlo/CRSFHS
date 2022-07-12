package com.example.crsfhs.android.activities

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.HairdresserDetails

class CustomAdapter(private val dataSet: ArrayList<HairdresserDetails>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(), Filterable {
    private val dataSetC: ArrayList<HairdresserDetails> = ArrayList<HairdresserDetails>()
    private var city: String

    init {
        dataSetC.addAll(dataSet)
        city = ""
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val hairdresser_location: TextView
        val hairdresser_name: TextView
        val hairdresser_icon: ImageView
        val hairdresser_rating: TextView
        val hairdresser_key: String

        init {
            hairdresser_location = view.findViewById(R.id.hairdresserAddress)
            hairdresser_name = view.findViewById(R.id.hairdresserName)
            hairdresser_icon = view.findViewById(R.id.hairdresserIcon)
            hairdresser_rating = view.findViewById(R.id.hairdresserRating)
            hairdresser_key = String()

            /*
            hairdresser_icon.setImageResource(R.drawable.test)
            hairdresser_icon.setBackgroundResource(R.drawable.rounded_corners_image)
            hairdresser_icon.scaleType = ImageView.ScaleType.CENTER_CROP
            hairdresser_icon.clipToOutline = true
             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hairdresserAddress =
            "${dataSetC[position].address.street} " +
                    "${dataSetC[position].address.number}, " +
                    "${dataSetC[position].address.postcode} " +
                    dataSetC[position].address.city

        holder.hairdresser_name.text = dataSetC[position].name
        holder.hairdresser_location.text = hairdresserAddress
        holder.hairdresser_rating.text = dataSetC[position].rating
        holder.hairdresser_icon.load(dataSetC[position].img.icon)
        holder.hairdresser_key.plus(dataSetC[position].key)
    }

    override fun getItemCount(): Int {
        return dataSetC.size
    }

    fun setCity(city: String){
        this.city = city
    }

    fun addItem(item: HairdresserDetails){
        dataSet.add(item)
        dataSetC.add(item)
        refresh()
    }

    fun addItems(array: ArrayList<HairdresserDetails>){
        dataSet.addAll(array)
        dataSetC.addAll(array)
        refresh()
    }

    fun setItems(array: ArrayList<HairdresserDetails>){
        dataSet.clear()
        dataSet.addAll(array)
        dataSetC.clear()
        dataSetC.addAll(array)
        refresh()
    }

    fun refresh(){
        dataSetC.sortByDescending { it.address.city.contains(city) }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val string = p0?.toString()?.lowercase() ?: ""
                val filteredList = ArrayList<HairdresserDetails>()

                if(string.isEmpty()){
                    filteredList.addAll(dataSet)
                }else{
                    dataSet.filter {
                        it.name.lowercase().startsWith(p0!!.toString(), true) ||
                                it.address.city.lowercase().startsWith(p0.toString(), true) ||
                                it.address.street.lowercase().startsWith(p0.toString(), true)
                    }.forEach { filteredList.add(it) }
                }
                return FilterResults().apply { values = filteredList}
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if(p1?.values == null){
                    dataSetC.clear()
                }else{
                    dataSetC.clear()
                    dataSetC.addAll(p1.values as ArrayList<HairdresserDetails>)
                }
                refresh()
            }
        }
    }
}
