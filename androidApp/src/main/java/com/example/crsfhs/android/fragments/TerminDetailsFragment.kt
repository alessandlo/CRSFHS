package com.example.crsfhs.android.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
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
import coil.load
import com.example.crsfhs.android.DatePickerFragment
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.MainActivity
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentTerminDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class TerminDetailsFragment : Fragment(R.layout.fragment_termin_details) {
    private var _binding: FragmentTerminDetailsBinding? = null
    private val binding get() = _binding!!
    var blocker = true
    private lateinit var mainPref: SharedPreferences

    override fun onCreateView(
        inflater:LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{
        _binding = FragmentTerminDetailsBinding.inflate(inflater, container, false)
        mainPref = (activity as MainActivity).getSharedPreferences("PREFERENCE",
            Context.MODE_PRIVATE
        )


        // image + friseurname + Adresse
        binding.hairdresserImage2.load(requireArguments().getString("imgLink"))
        binding.hairdresserName3.text = requireArguments().getString("friseurname")
        binding.hairdresserAddress3.text = requireArguments().getString("adresse")

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
                        val newDate = "$dateFrag3-$dateFrag2-$dateFrag"
                        val current = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val today = current.format(formatter)
                        if(newDate < today){ //checks if the date is in the past
                            Toast.makeText(requireActivity(),"Das eingegeben Datum liegt in der Vergangenheit", Toast.LENGTH_LONG).show()
                        }else {
                            binding.tdbtn1.text = date.toString()
                            Times.setText("Zeiten")
                            getTime(day, date.substring(4, 14))
                        }
                    }
                }
                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
                binding.Times.setOnItemClickListener { parent, view, position, id ->
                    val selectedTime = parent.getItemAtPosition(position).toString() // get selected timeslot
                    binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                        val selectedService = parent.getItemAtPosition(position).toString()
                        tdbtn2.setOnClickListener {
                            val dateFrag = binding.tdbtn1.text.substring(4, 6)
                            val dateFrag2 = binding.tdbtn1.text.substring(7, 9)
                            val dateFrag3 = binding.tdbtn1.text.substring(10, 14)
                            val newDate = "$dateFrag3-$dateFrag2-$dateFrag $selectedTime"
                            val current = ZonedDateTime.now(ZoneId.of("Europe/Paris"))
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                            val today = current.format(formatter)
                            //jump to next fragment and transfer appointment details
                            if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt

                                mainPref.edit().putString("redirLogin", "true").apply()
                                mainPref.edit().putString("imgLink", requireArguments().getString("imgLink")).apply()
                                mainPref.edit().putString("friseurname", binding.hairdresserName3.text.toString()).apply()
                                mainPref.edit().putString("adresse", binding.hairdresserAddress3.text.toString()).apply()
                                mainPref.edit().putString("date", binding.tdbtn1.text.toString()).apply()
                                mainPref.edit().putString("time", binding.Times.text.toString()).apply()
                                mainPref.edit().putString("service", selectedService).apply()
                                mainPref.edit().putString("hairsalon_key", requireArguments().getString("hairsalon_key")!!).apply()


                                findNavController().navigate(R.id.action_fragment_termin_details_to_fragment_login)
                            }else if(newDate < today){
                                Toast.makeText(requireActivity(),"Sie haben einen Termin in der Vergangenheit eingegeben.", Toast.LENGTH_LONG).show()
                                binding.tdbtn1.text = "Datum auswählen"
                            }else if(binding.Times.text.toString() == "Zeiten"){
                                Toast.makeText(requireActivity(),"Ungültige Eingabe.", Toast.LENGTH_LONG).show()
                            }
                            else
                                findNavController().navigate(R.id.action_termin_details_to_zusammenfassung, Bundle().apply {
                                    putString("imgLink", requireArguments().getString("imgLink"))
                                    putString("friseurname", binding.hairdresserName3.text.toString())
                                    putString("adresse", binding.hairdresserAddress3.text.toString())
                                    putString("date", binding.tdbtn1.text.toString())
                                    putString("time", binding.Times.text.toString())
                                    putString("service", selectedService)
                                    putString("hairsalon_key",  requireArguments().getString("hairsalon_key")!!)
                                })
                            Times.setText("Zeiten")
                            autoCompleteTextView.setText("Services")
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun getTime(day : String, date: String ){

        val retrofitData = DbApi.retrofitService.getHairdresser(requireArguments().getString("hairsalon_key")!!)
        retrofitData.enqueue(object : Callback<HairdresserDetails> {
            override fun onResponse(
                call: Call<HairdresserDetails?>,
                response: Response<HairdresserDetails?>
            ) {
                var openingtime: String? = null
                var closingtime: String? = null

                when(day){
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
                       // checkTime(date, openingtime)
                       if(true) {
                           timeslots.add(openingtime)
                       }
                       val df = SimpleDateFormat("HH:mm")
                       val d = df.parse(timestamp)
                       val cal = Calendar.getInstance()
                       cal.setTime(d)
                       cal.add(Calendar.MINUTE, 30)
                       timestamp = df.format(cal.getTime())
                       openingtime = timestamp.toString()

                   }
                    onResume(timeslots) // Fun aufrufen um dropdown menu zu füllen
            }
                val services: ArrayList<String> = ArrayList()
                response.body()!!.services.forEach {
                    val name = it.name
                    services.add("$name")
                }
                onResume1(services)
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
        binding.Times.setAdapter(arrayAdapter)
    }

    fun onResume1(services : ArrayList<String>) {
        super.onResume()
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.servicelist, services )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
