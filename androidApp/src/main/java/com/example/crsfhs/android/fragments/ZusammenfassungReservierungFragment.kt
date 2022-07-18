package com.example.crsfhs.android.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.MainActivity
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentZusammenfassungReservierungBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ZusammenfassungReservierungFragment : Fragment() {
    private var _binding: FragmentZusammenfassungReservierungBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //getInfo()
        return inflater.inflate(R.layout.fragment_zusammenfassung_reservierung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentZusammenfassungReservierungBinding.bind(view)
        mainPref = (activity as MainActivity).getSharedPreferences("PREFERENCE",
            Context.MODE_PRIVATE
        )

        if (mainPref.getString("redirLogin", "false") == "true") {
            binding.hairdresserImage3.load(mainPref.getString("imgLink", "false"))
            binding.hairdresserName2.text = mainPref.getString("friseurname", "failed to load")
            binding.hairdresserAddress2.text = mainPref.getString("adresse", "failed to load")
        }
        else {
            binding.hairdresserImage3.load(requireArguments().getString("imgLink"))
            binding.hairdresserName2.text = requireArguments().getString("friseurname")
            binding.hairdresserAddress2.text = requireArguments().getString("adresse")
        }

        binding.apply {
            if (mainPref.getString("redirLogin", "false") == "true") {
                textView2.text = mainPref.getString("date", "failed to load")
                textView3.text = mainPref.getString("time", "failed to load")
                textView4.text = mainPref.getString("service", "failed to load")
            } else {
                textView2.text = "Datum: " + requireArguments().getString("date").toString()
                textView3.text = "Uhrzeit: " + requireArguments().getString("time").toString()
                textView4.text = "Service: " + requireArguments().getString("service").toString()
            }


            button.setOnClickListener {         //jump to next fragment and transfer appointment details
                val df = SimpleDateFormat("HH:mm")
                val d = df.parse(textView3.text.toString())
                val cal = Calendar.getInstance()
                cal.time = d
                cal.add(Calendar.MINUTE, 30)
                val time_to = df.format(cal.getTime())

                //val formattedDate = requireArguments().getString("date").toString().substring(4, 14)
                val formattedDate = textView2.text.toString().substring(4, 14)

                Log.d(TAG, formattedDate)

                val reservationItem = ReservationItem(
                    ReservationDetails(
                        appointment = ReservationAppointment(
                            date = formattedDate,
                            status = "aktiv",
                            time_from = textView3.text.toString(),
                            time_to = time_to,
                            service = textView4.text.toString()
                        ),
                        hairdresser_key =  mainPref.getString("hairsalon_key", "failed to load").toString(),
                        key = null,
                        user_key = loggedInUserKey.toString()
                    )
                )
                val retrofitData = DbApi.retrofitService.saveAppointment(reservationItem)
                retrofitData.enqueue(object : Callback<ReservationDetails?> {
                    override fun onResponse(
                        call: Call<ReservationDetails?>,
                        response: Response<ReservationDetails?>
                    ) {
                        Toast.makeText(activity, "Termin erstellt!", Toast.LENGTH_LONG).show()
                        Log.i("Reserve", "Appointment created")

                        //globale Variablen löschen
                        mainPref.edit().putString("redirLogin", "false").apply()
                        mainPref.edit().putString("imgLink", "").apply()
                        mainPref.edit().putString("friseurname", "").apply()
                        mainPref.edit().putString("adresse", "").apply()
                        mainPref.edit().putString("date", "").apply()
                        mainPref.edit().putString("time", "").apply()
                        mainPref.edit().putString("service", "").apply()
                        mainPref.edit().putString("hairsalon_key", "").apply()

                        findNavController().navigate(R.id.action_fragment_zusammenfassung_reservierung_to_fragment_startseite)
                    }

                    override fun onFailure(call: Call<ReservationDetails?>, t: Throwable) {
                        Log.e("Reserve", "onFailure: " + t.message)
                    }

                })

            }

        }
    }
}

