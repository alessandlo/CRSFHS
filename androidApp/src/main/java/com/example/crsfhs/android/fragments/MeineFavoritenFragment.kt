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
import com.example.crsfhs.android.adapter.FavoritesAdapter
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentMeineFavoritenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeineFavoritenFragment : Fragment() {
    private lateinit var binding: FragmentMeineFavoritenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeineFavoritenBinding.inflate(inflater, container, false)

        if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)
        } else {
            getData()
        }

        return binding.root
    }

    private fun getData() {
        val userkey = FavoritesByUser(listOf(FavoriteQueryUserkey(loggedInUserKey!!)))
        val retrofitData = DbApi.retrofitService.getFavoritesByUserkey(userkey)

        retrofitData.enqueue(object : Callback<FavoritesList?> {
            override fun onResponse(
                call: Call<FavoritesList?>,
                response: Response<FavoritesList?>
            ) {
                Log.i("Favorites", response.body().toString())
                val hairdresserList: ArrayList<HairdresserDetails> = ArrayList()

                response.body()!!.items.forEach {
                    val retrofitData2 = DbApi.retrofitService.getHairdresser(it.hairdresser_key)
                    retrofitData2.enqueue(object : Callback<HairdresserDetails?> {
                        override fun onResponse(
                            call2: Call<HairdresserDetails?>,
                            response2: Response<HairdresserDetails?>
                        ) {
                            hairdresserList.add(
                                response2.body()!!
                            )

                            hairdresserList.sortByDescending { hairdresser ->
                                hairdresser.name
                            }

                            hairdresserList.let {
                                val adapter = FavoritesAdapter(hairdresserList) { key ->
                                    val bundle = bundleOf(
                                        "hairsalon_key" to key
                                    )
                                    binding.root.findNavController().navigate(R.id.action_fragment_meine_favoriten_to_fragment_friseursalon, bundle)
                                }
                                val recyclerView = binding.favoritesRv
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

            override fun onFailure(call: Call<FavoritesList?>, t: Throwable) {
                Log.e("Favorite", "onFailure: " + t.message)
            }
        })
    }
}
