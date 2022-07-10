package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crsfhs.android.R
import com.example.crsfhs.android.ReservationAdapter
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentMeineReservierungenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MeineReservierungenFragment : Fragment() {
    private lateinit var binding: FragmentMeineReservierungenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeineReservierungenBinding.inflate(inflater, container, false)

        if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)

            println(loggedInUserKey)
        } else {
            getData()
        }
        return binding.root
    }

    private fun getData() {
        val userkey = ReservationByUser(listOf(ReservationQuery(loggedInUserKey!!)))
        val retrofitData = DbApi.retrofitService.getReservationsByUserkey(userkey)

        retrofitData.enqueue(object : Callback<ReservationsList?> {
            override fun onResponse(
                call: Call<ReservationsList?>,
                response: Response<ReservationsList?>
            ) {
                val appointmentList: ArrayList<Appointment> = ArrayList()

                response.body()!!.items.forEach {
                    if (it.appointment.status == "aktiv") {
                        val key = it.key
                        val date = it.appointment.date
                        val time = it.appointment.time_to
                        changeStatus(key!!, date, time)
                    }

                    val retrofitData2 = DbApi.retrofitService.getHairdresser(it.hairdresser_key)
                    retrofitData2.enqueue(object : Callback<HairdresserDetails?> {
                        override fun onResponse(
                            call2: Call<HairdresserDetails?>,
                            response2: Response<HairdresserDetails?>
                        ) {
                            appointmentList.add(
                                Appointment(
                                    hairdresserDetails = response2.body()!!,
                                    reservationDetails = it
                                )
                            )

                            appointmentList.sortByDescending { appointment ->
                                val date = appointment.reservationDetails.appointment.date
                                val time = appointment.reservationDetails.appointment.time_to
                                val appointmentDateTime =
                                    LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

                                appointmentDateTime
                            }

                            appointmentList.let {
                                val adapter = ReservationAdapter(appointmentList) { appointment ->
                                    val bundle = bundleOf(
                                        "reservation_key" to appointment.reservationDetails.key,
                                        "hairdresser_name" to appointment.hairdresserDetails.name,
                                        "appointment_date" to appointment.reservationDetails.appointment.date,
                                        "appointment_time_from" to appointment.reservationDetails.appointment.time_from,
                                        "appointment_time_to" to appointment.reservationDetails.appointment.time_to,
                                        "appointment_status" to appointment.reservationDetails.appointment.status,
                                        "hairdresser_city" to appointment.hairdresserDetails.address.city,
                                        "hairdresser_number" to appointment.hairdresserDetails.address.number,
                                        "hairdresser_postcode" to appointment.hairdresserDetails.address.postcode,
                                        "hairdresser_street" to appointment.hairdresserDetails.address.street,
                                        "hairdresser_latitude" to appointment.hairdresserDetails.address.latitude,
                                        "hairdresser_longitude" to appointment.hairdresserDetails.address.longitude
                                    )
                                    binding.root.findNavController()
                                        .navigate(R.id.action_mr_to_mrv, bundle)
                                }
                                val recyclerView = binding.reservationsRv
                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.adapter = adapter
                            }
                        }

                        override fun onFailure(call: Call<HairdresserDetails?>, t: Throwable) {
                            Log.e("Hairdresser", "onFailure: " + t.message)
                        }
                    })
                }
            }

            override fun onFailure(call: Call<ReservationsList?>, t: Throwable) {
                Log.e("Reservation", "onFailure: " + t.message)
            }
        })
    }

    private fun changeStatus(key: String, date: String, time: String) {
        val current = LocalDateTime.now()
        val appointmentDateTime =
            LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

        if (current.isAfter(appointmentDateTime)) {
            val retrofitData =
                DbApi.retrofitService.changeStatus(key, Status(null, SetStatus("vergangen")))

            retrofitData.enqueue(object : Callback<Status?> {
                override fun onResponse(
                    call: Call<Status?>,
                    response: Response<Status?>
                ) {
                    Log.i("Status", "vergangen")
                }

                override fun onFailure(call: Call<Status?>, t: Throwable) {
                    Log.e("Status", "onFailure: " + t.message)
                }
            })
        }
        //Log.i("Current DateTime", current.toString())
        //Log.i("DateTime", appointmentDateTime.toString())
    }
}
