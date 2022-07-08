package com.example.crsfhs.android.fragments

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.DatePickerFragment
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentTerminDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
                        val dateFrag = date.substring(4, 6)
                        val dateFrag2 = date.substring(7, 9)
                        val dateFrag3 = date.substring(10, 14)
                        val newDate = dateFrag3+"-"+dateFrag2+"-"+dateFrag
                        val current = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val today = current.format(formatter)
                        if(newDate < today){ //checks if the date is in the past
                            Toast.makeText(requireActivity(),"Das eingegeben Datum liegt in der Vergangenheit", Toast.LENGTH_LONG).show()
                        }else {
                            tvDate.text = date
                            getTime(day)
                        }
                    }
                }
                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                    val selectedtime = parent.getItemAtPosition(position).toString() // get selected timeslot
                    tdbtn2.setOnClickListener {         //jump to next fragment and transfer appointment details
                        if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
                            findNavController().navigate(R.id.action_fragment_termin_details_to_fragment_login)
                        }
                        else
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

               if(openingtime == "-" && closingtime == "-"){
                    Toast.makeText(requireActivity(),"Der Friseur hat am "+ date +" geschlossen!", Toast.LENGTH_LONG).show()
                } else{

                    val timeslots = ArrayList<String>()
                   var timestamp = openingtime
                   while(openingtime!! < closingtime!!.toString()){ //Liste mit Zeiten füllen mit Zeiten im 30 min Takt
                       timeslots.add(openingtime)
                       val df = SimpleDateFormat("HH:mm")
                       val d = df.parse(timestamp)
                       val cal = Calendar.getInstance()
                       cal.setTime(d)
                       cal.add(Calendar.MINUTE, 30)
                       timestamp = df.format(cal.getTime())
                       openingtime = timestamp.toString()
                       Log.d(TAG, openingtime)

                   }
                    onResume(timeslots) // Fun aufrufen um dropdown menu zu füllen
            }}


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
