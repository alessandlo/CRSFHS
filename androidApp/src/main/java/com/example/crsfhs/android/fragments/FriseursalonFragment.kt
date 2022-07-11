package com.example.crsfhs.android.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.DbApi
import com.example.crsfhs.android.api.HairdresserDetails
import com.example.crsfhs.android.databinding.FragmentFriseursalonBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriseursalonFragment : Fragment() {

    private lateinit var binding: FragmentFriseursalonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriseursalonBinding.inflate(inflater, container, false)
        val retrofitData = DbApi.retrofitService.getHairdresser("asru6sxqrifl")
        retrofitData.enqueue(object : Callback<HairdresserDetails> {
            override fun onResponse(
                call: Call<HairdresserDetails?>,
                response: Response<HairdresserDetails?>
            ) {
                // Name
                val name = response.body()?.name
                setName(name!!)
                //Addresse
                val street = response.body()?.address?.street
                val number = response.body()?.address?.number
                val zip = response.body()?.address?.postcode
                val city = response.body()?.address?.city
                val address = street+ " "+ number +", "+ zip + ", "+ city
                setText(address)
                val openings= "Mo: \t"+ response.body()?.openings?.Mo?.time_from +"-"+ response.body()?.openings?.Mo?.time_to +"\n"+    //Montag Öffnungszeiten
                        "Di:\t\t"+ response.body()?.openings?.Di?.time_from +"-"+ response.body()?.openings?.Di?.time_to +"\n"+   //Dienstag Öffnungszeiten
                        "Mi:\t\t"+ response.body()?.openings?.Mi?.time_from +"-"+ response.body()?.openings?.Mi?.time_to +"\n"+   //Mittwoch Öffnungszeiten
                        "Do:\t\t"+ response.body()?.openings?.Do?.time_from +"-"+ response.body()?.openings?.Do?.time_to +"\n"+   //Donnerstag Öffnungszeiten
                        "Fr:\t\t"+ response.body()?.openings?.Fr?.time_from +"-"+ response.body()?.openings?.Fr?.time_to +"\n"+   //Freitag Öffnungszeiten
                        "Sa:\t\t"+ response.body()?.openings?.Sa?.time_from +"-"+ response.body()?.openings?.Sa?.time_to +"\n"+   //Samstag Öffnungszeiten
                        "So:\t\t"+ response.body()?.openings?.So?.time_from +"-"+ response.body()?.openings?.So?.time_to +"\n"    //Sonntag Öffnungszeiten
                setOpenings(openings)
                //services
                val services : ArrayList<String> = ArrayList(20)
                var gender = ""
                response.body()!!.services.forEach {
                    val gender1 = it.gender
                    val name = it.name
                    val time = it.time
                    val price = it.price
                    if(gender1 == "male"){
                         gender = "Herren"
                    }else{
                        gender = "Damen"
                    }
                    services.add("$name, $gender, $time Minuten, $price")
                }
                setServices(services)
                //Image
                val url = response.body()?.img?.logo
                Picasso.get().load(url).into(view?.findViewById(R.id.hairsalon_picture))
            }
            override fun onFailure(call: Call<HairdresserDetails>, t: Throwable) {
                Log.e("Load Time", "onFailure: " + t.message)
            }
        })
        binding.sharebtn.setOnClickListener{
            // Text rausholen
            val s = binding.hairsalonName.text.toString()

            // Share Intent
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Empfehlung geht raus!")
                putExtra(Intent.EXTRA_TEXT, s)
                putExtra(Intent.EXTRA_STREAM,Uri.parse("/drawable/fiverr_reshairvation_r1_02_final.png"))
                type = "*/*"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        binding.resbtn.setOnClickListener {
            findNavController().navigate(R.id.action_friseursalon_to_termindetails)
        }
        // Inflate the layout for this fragment
        return binding.root
    }



    private fun setServices(services: ArrayList<String>) {
        val lv = view?.findViewById(R.id.Services) as ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, services)
        lv.adapter = adapter
    }

    //Funktion um Textfeld mit dem Namen zu füllen
    private fun setOpenings(openings: String) {
        binding.textView9.setText(openings)
    }
    //Funktion um Textfeld mit der Adresse zu füllen
    private fun setText(address: String) {
        binding.textView2.setText(address)
    }
    //Funktion um Textfeld mit den Öffnungszeiten zu füllen
    private fun setName(name : String) {
        binding.hairsalonName.setText(name)
    }
}
