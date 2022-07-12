package com.example.crsfhs.android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crsfhs.android.adapter.ReservationAdapterHairdresser
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentHairsalonBevorstehendeReservierungenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HairsalonBevorstehendeReservierungenFragment : Fragment() {
    private lateinit var binding: FragmentHairsalonBevorstehendeReservierungenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHairsalonBevorstehendeReservierungenBinding.inflate(inflater, container, false)

        getData()

        return binding.root
    }

    private fun getData() {
        val hairdresserkey =
            ReservationByHairdresser(listOf(ReservationQueryHairdresser("asru6sxqrifl")))
        val retrofitData = DbApi.retrofitService.getReservationsByHairdresserkey(hairdresserkey)

        retrofitData.enqueue(object : Callback<ReservationsList?> {
            override fun onResponse(
                call: Call<ReservationsList?>,
                response: Response<ReservationsList?>
            ) {
                val appointmentList: ArrayList<AppointmentHairdresser> = ArrayList()

                response.body()!!.items.forEach {
                    if (it.appointment.status == "aktiv") {
                        val key = it.key
                        val date = it.appointment.date
                        val time = it.appointment.time_to
                        changeStatus(key!!, date, time)
                    }

                    val retrofitData2 = DbApi.retrofitService.getUser(it.user_key)
                    retrofitData2.enqueue(object : Callback<UserDetails?> {
                        override fun onResponse(
                            call2: Call<UserDetails?>,
                            response2: Response<UserDetails?>
                        ) {
                            appointmentList.add(
                                AppointmentHairdresser(
                                    userDetails = response2.body()!!,
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
                                val adapter = ReservationAdapterHairdresser(appointmentList)
                                val recyclerView = binding.reservationsRv
                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.adapter = adapter
                            }
                        }

                        override fun onFailure(call: Call<UserDetails?>, t: Throwable) {
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
    }
}
