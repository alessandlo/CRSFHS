package com.example.crsfhs.android.activities

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.HairdresserDetails

class CustomAdapter(private val dataSet: ArrayList<HairdresserDetails>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(), Filterable {
    private val dataSetC: ArrayList<HairdresserDetails> = ArrayList<HairdresserDetails>()

    init {
        dataSetC.addAll(dataSet)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val hairdresser_location: TextView
        val hairdresser_name: TextView
        val hairdresser_icon: ImageView

        init {
            hairdresser_location = view.findViewById(R.id.hairdresser_location)
            hairdresser_name = view.findViewById(R.id.hairdresser_name)
            hairdresser_icon = view.findViewById(R.id.hairdresser_icon)

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
            .inflate(R.layout.custom_rv_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hairdresser_name.text = dataSetC[position].name
        holder.hairdresser_location.text = dataSetC[position].address.city
        holder.hairdresser_icon.load(dataSetC[position].img.icon)
    }

    override fun getItemCount(): Int {
        return dataSetC.size
    }

    fun addItems(array: ArrayList<HairdresserDetails>){
        dataSet.addAll(array)
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
                        it.name.lowercase().contains(p0!!.toString().lowercase())
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
                notifyDataSetChanged()
            }
        }
    }
}
