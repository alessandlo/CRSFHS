package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.DatePickerFragment
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentTerminDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TerminDetailsFragment : Fragment(R.layout.fragment_termin_details) {
    private var _binding: FragmentTerminDetailsBinding? = null
    private val binding get() = _binding!!


//opens up the datepicker and works with the output
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    )
    {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTerminDetailsBinding.bind(view)

        binding.apply {
            tdbtn1.setOnClickListener {
                // create new instance of DatePickerFragment
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {

                        val date = bundle.getString("SELECTED_DATE") // Day + Date > printed above button
                        val day = date!!.take(2) // shortened string to just abbreviation of day

                        tvDate.text = date
                        getTime(day)
                    }
                }
                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                    val selectedtime = parent.getItemAtPosition(position).toString() // get selected timeslot
                    tdbtn2.setOnClickListener {         //jump to next fragment and transfer appointment details
                        findNavController().navigate(R.id.action_termin_details_to_zusammenfassung, Bundle().apply {
                            putString("date", tvDate.text.toString())
                            putString("time", selectedtime)
                            putString("comment", Kommentarzeile.text.toString())
                        })
                    }
                }
            }
        }
    }


    private fun getTime(date : String){

        //val retrofitData = DbApi.retrofitService.getTimes("asru6sxqrifl", date)
        val retrofitData = DbApi.retrofitService.getHairdresser("asru6sxqrifl")
        retrofitData.enqueue(object : Callback<HairdresserDetails> {
            override fun onResponse(
                call: Call<HairdresserDetails?>,
                response: Response<HairdresserDetails?>
            ) {
                var openingtime: String? = null
                var closingtime: String? = null

                when(date){
                    "Mo" -> {
                        openingtime = response.body()!!.openings.Mo.time_from
                        closingtime = response.body()!!.openings.Mo.time_to
                    }
                    "Di" -> {
                        openingtime = response.body()!!.openings.Di.time_from
                        closingtime = response.body()!!.openings.Di.time_to
                    }
                    "Mi" -> {
                        openingtime = response.body()!!.openings.Mi.time_from
                        closingtime = response.body()!!.openings.Mi.time_to
                    }
                    "Do" -> {
                        openingtime = response.body()!!.openings.Do.time_from
                        closingtime = response.body()!!.openings.Do.time_to
                    }
                    "Fr" -> {
                        openingtime = response.body()!!.openings.Fr.time_from
                        closingtime = response.body()!!.openings.Fr.time_to
                    }
                    "Sa" -> {
                        openingtime = response.body()!!.openings.Sa.time_from
                        closingtime = response.body()!!.openings.Sa.time_to
                    }
                    "So" -> {
                        openingtime = response.body()!!.openings.So.time_from
                        closingtime = response.body()!!.openings.So.time_to
                    }
                }

                Log.i("Zeiten", openingtime + " " + closingtime)

                //var openingtime  = "10:00"//response.body()?.time_from!! //Kommentar sollte die eigentliche Variable sein, gibt aber null zurück deswegen statisch for now
                //val closingtime = "17:00"//response.body()?.time_to  //Kommentar sollte die eigentliche Variable sein, gibt aber null zurück deswegen statisch for now
                
                var counter = 0
                val timeslots = ArrayList<String>()
                var timestamp = openingtime!!.take(2).toInt()
                while("$timestamp:00" < closingtime!!) // While schleife um Zeiten einzutragen in ArrayList
                    if(counter % 2 == 0 && counter > 0){ // für gerade Zeiten (Bsp. 11:00)
                        timestamp++ // erhöht stunde um 1
                        openingtime = timestamp.toString()
                        openingtime = openingtime.take(2)
                        val time = "$openingtime:00"
                        timeslots.add(time) // neuer eintrag in ArrayList
                        counter++
                    }else if(counter % 2 !== 0){ // für ungerade Zeiten (Bsp. 11:30)
                        openingtime = openingtime!!.take(2)
                        val time = "$openingtime:30"
                        timeslots.add(time)
                        counter++
                    }else{ // für die Öffnungszeit (hier 10:00)
                        val time = openingtime
                        timeslots.add(time!!)
                        counter++
                    }
            onResume(timeslots) // Fun aufrufen um dropdown menu zu füllen
            }


            override fun onFailure(call: Call<HairdresserDetails>, t: Throwable) {
                Log.e("Load Time", "onFailure: " + t.message)
            }
        })
    }

    //fills the dropdown menu with content
     fun onResume(timeslots : ArrayList<String>) {
        super.onResume()

        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdownzeiten, timeslots )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
    override fun onCreateView(
        inflater:LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{
        _binding = FragmentTerminDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
