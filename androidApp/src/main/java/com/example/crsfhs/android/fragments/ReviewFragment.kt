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
import com.example.crsfhs.android.ReviewAdapter
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentReviewsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)

        if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)

            println(loggedInUserKey)
        } else {
            getData()
        }
        return binding.root
    }

    private fun getData(){
        val userkey = ReviewByUser(listOf(ReviewQuery(loggedInUserKey!!)))
        val retrofitData = DbApi.retrofitService.getReviewsByUserkey(userkey)

        retrofitData.enqueue(object : Callback<ReviewList?> {
            override fun onResponse(
                call: Call<ReviewList?>,
                response: Response<ReviewList?>
            ) {
                val reviewList: ArrayList<Review> = ArrayList()

                response.body()!!.items.forEach {
                    val retrofitData2 = DbApi.retrofitService.getHairdresser(it.hairdresser_key)
                    retrofitData2.enqueue(object : Callback<HairdresserDetails?> {
                        override fun onResponse(
                            call2: Call<HairdresserDetails?>,
                            response2: Response<HairdresserDetails?>
                        ) {
                            reviewList.add(
                                Review(
                                    hairdresserDetails = response2.body()!!,
                                    reviewDetails = it
                                )
                            )

                            reviewList.sortByDescending { review ->
                                val date = review.reviewDetails.date
                                val reviewDate =
                                    LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                                reviewDate
                            }

                            reviewList.let {
                                val adapter = ReviewAdapter(reviewList)
                                val recyclerView = binding.reviewsRv
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

            override fun onFailure(call: Call<ReviewList?>, t: Throwable) {
                Log.e("Review", "onFailure: " + t.message)
            }
        })
    }
}
