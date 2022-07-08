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

        return inflater.inflate(R.layout.fragment_zusammenfassung_reservierung, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentZusammenfassungReservierungBinding.bind(view)

        binding.apply {
            textView2.text = requireArguments().getString("date").toString()
            textView3.text = requireArguments().getString("time").toString()
            textView4.text = requireArguments().getString("comment").toString()

            button.setOnClickListener {         //jump to next fragment and transfer appointment details
                val df = SimpleDateFormat("HH:mm")
                val d = df.parse(textView3.text as String)
                val cal = Calendar.getInstance()
                cal.setTime(d)
                cal.add(Calendar.MINUTE, 30)
                val time_to = df.format(cal.getTime())
                val formattedDate = textView2.text.toString().substring(4, 14)
                Log.d(TAG, formattedDate)

                val reservationItem = ReservationItem(
                    ReservationDetails(
                        appointment = ReservationAppointment(formattedDate, "aktiv",textView3.text.toString(), time_to),
                        hairdresser_key = "asru6sxqrifl",
                        key = null,
                        user_key = loggedInUserKey.toString()
                    )
                )
                val retrofitData = DbApi.retrofitService.saveAppointment(reservationItem)
                retrofitData.enqueue(object : Callback<ReservationDetails?> {
                    override fun onResponse(call: Call<ReservationDetails?>, response: Response<ReservationDetails?>) {
                        Toast.makeText(activity, "Termin erstellt!", Toast.LENGTH_SHORT).show()
                        Log.i("Reserve", "Appointment created")
                        findNavController().navigate(R.id.action_zusammenfassung_to_wurde_reserviert)
                    }

                    override fun onFailure(call: Call<ReservationDetails?>, t: Throwable) {
                        Log.e("Reserve", "onFailure: " + t.message)
                    }

                })

            }

        }
    }
}

