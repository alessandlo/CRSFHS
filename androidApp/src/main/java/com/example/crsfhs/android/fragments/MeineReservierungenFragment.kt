package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MeineReservierungenFragment : Fragment() {
    private lateinit var binding: FragmentMeineReservierungenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeineReservierungenBinding.inflate(inflater, container, false)

        if(!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)

            println(loggedInUserKey)
        }
        else {
            getData(loggedInUserKey)
        }
        return binding.root
    }

    private fun getData(userKey: String?) {
        val userkey = ReservationByUser(listOf(ReservationQuery("6fv97g84s9ud")))
        val retrofitData = DbApi.retrofitService.getReservartionsByUserkey(userkey)

        retrofitData.enqueue(object : Callback<ReservationsList?> {
            override fun onResponse(
                call: Call<ReservationsList?>,
                response: Response<ReservationsList?>
            ) {
                val appointmentList: MutableList<Appointment> = mutableListOf()
                response.body()!!.items.forEach {
                    val retrofitData2 = DbApi.retrofitService.getHairdresser(it.hairdresser_key)
                    retrofitData2.enqueue(object : Callback<HairdresserDetails?> {
                        override fun onResponse(
                            call2: Call<HairdresserDetails?>,
                            response2: Response<HairdresserDetails?>
                        ) {
                            appointmentList.add(
                                Appointment(
                                    hairdresserDetails = response2.body()!!,
                                    reservationsDetails = it
                                )
                            )

                            val result = response.body()?.items
                            result.let {
                                val adapter = ReservationAdapter(appointmentList)
                                val recyclerView = binding.reservationsRv
                                recyclerView.layoutManager = LinearLayoutManager(activity)
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
}
