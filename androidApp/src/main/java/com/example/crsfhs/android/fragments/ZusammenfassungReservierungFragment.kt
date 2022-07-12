package com.example.crsfhs.android.fragments

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.crsfhs.android.R
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

        binding.hairdresserImage3.load(requireArguments().getString("imgLink"))
        binding.hairdresserName2.text = requireArguments().getString("friseurname")
        binding.hairdresserAddress2.text = requireArguments().getString("adresse")

        binding.apply {
            textView2.text = "Datum: "+requireArguments().getString("date").toString()
            textView3.text = "Uhrzeit: "+requireArguments().getString("time").toString()
            textView4.text = "Kommentar: "+requireArguments().getString("comment").toString()



            button.setOnClickListener {         //jump to next fragment and transfer appointment details
                val df = SimpleDateFormat("HH:mm")
                val d = df.parse(requireArguments().getString("time").toString())
                val cal = Calendar.getInstance()
                cal.time = d
                cal.add(Calendar.MINUTE, 30)
                val time_to = df.format(cal.getTime())
                val formattedDate = requireArguments().getString("date").toString().substring(4, 14)
                Log.d(TAG, formattedDate)

                val reservationItem = ReservationItem(
                    ReservationDetails(
                        appointment = ReservationAppointment(
                            formattedDate,
                            "aktiv",
                            requireArguments().getString("time").toString(),
                            time_to
                        ),
                        hairdresser_key = "asru6sxqrifl",
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
                        findNavController().navigate(R.id.action_fragment_zusammenfassung_reservierung_to_fragment_startseite)
                    }

                    override fun onFailure(call: Call<ReservationDetails?>, t: Throwable) {
                        Log.e("Reserve", "onFailure: " + t.message)
                    }

                })

            }

        }
    }

    /*
//receives the Information on the Hairdresser for the Reservation
    private fun getInfo() {

        val retrofitData = DbApi.retrofitService.getHairdresser("asru6sxqrifl")
        retrofitData.enqueue(object : Callback<HairdresserDetails> {
            override fun onResponse(
                call: Call<HairdresserDetails?>,
                response: Response<HairdresserDetails?>
            ) {
                val name = response.body()?.name
                setName(name!!)
                val street = response.body()?.address?.street
                val number = response.body()?.address?.number
                val zip = response.body()?.address?.postcode
                val city = response.body()?.address?.city
                val address = "$street $number, $zip, $city"
                setAddress(address)
                val url = response.body()?.img?.logo
                //binding.imageView.load(url)
            }
            override fun onFailure(call: Call<HairdresserDetails>, t: Throwable) {
                Log.e("Load Time", "onFailure: " + t.message)
            }
        })
    }
    private fun setAddress(address: String) {
        binding.textView10.text = address
    }
    private fun setName(name : String) {
        binding.textView11.text = name
    }
    */

}

