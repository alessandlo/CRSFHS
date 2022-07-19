package com.example.crsfhs.android.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentFriseursalonBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriseursalonFragment : Fragment() {
    private lateinit var binding: FragmentFriseursalonBinding
    private lateinit var url: String
    /*private lateinit var street: String
    private lateinit var number: String
    private lateinit var zip: String
    private lateinit */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriseursalonBinding.inflate(inflater, container, false)

        val retrofitData = DbApi.retrofitService.getHairdresser(requireArguments().getString("hairsalon_key")!!)
        retrofitData.enqueue(object : Callback<HairdresserDetails> {
            override fun onResponse(
                call: Call<HairdresserDetails?>,
                response: Response<HairdresserDetails?>
            ) {
                // Name
                println(response.message())
                binding.hairdresserName.text = response.body()!!.name

                //Addresse
                val street = response.body()!!.address.street
                val number = response.body()!!.address.number
                val zip = response.body()!!.address.postcode
                val city = response.body()!!.address.city
                binding.hairdresserAddress.text = "$street $number, $zip, $city"

                val openings =
                    "Mo: " + response.body()?.openings?.Mo?.time_from + "-" + response.body()?.openings?.Mo?.time_to + "\n" +    //Montag Öffnungszeiten
                            "Di: \t\t" + response.body()?.openings?.Di?.time_from + "-" + response.body()?.openings?.Di?.time_to + "\n" +   //Dienstag Öffnungszeiten
                            "Mi: \t\t" + response.body()?.openings?.Mi?.time_from + "-" + response.body()?.openings?.Mi?.time_to + "\n" +   //Mittwoch Öffnungszeiten
                            "Do: \t" + response.body()?.openings?.Do?.time_from + "-" + response.body()?.openings?.Do?.time_to + "\n" +   //Donnerstag Öffnungszeiten
                            "Fr: \t\t" + response.body()?.openings?.Fr?.time_from + "-" + response.body()?.openings?.Fr?.time_to + "\n" +   //Freitag Öffnungszeiten
                            "Sa:\t\t" + response.body()?.openings?.Sa?.time_from + "-" + response.body()?.openings?.Sa?.time_to + "\n" +   //Samstag Öffnungszeiten
                            "So:\t\t" + response.body()?.openings?.So?.time_from + "-" + response.body()?.openings?.So?.time_to + "\n"    //Sonntag Öffnungszeiten
                binding.hairdresserOpenings.text = openings

                //services
                val services: ArrayList<String> = ArrayList()
                var gender = ""
                response.body()!!.services.forEach {
                    val gender1 = it.gender
                    val name = it.name
                    val time = it.time
                    val price = it.price
                    when (gender1) {
                        "male" -> gender = "Herren"
                        "female" -> gender = "Damen"
                    }
                    services.add("$name, $gender, $time Minuten, $price")
                }
                setServices(services)

                //Image
                url = response.body()?.img?.logo.toString()
                binding.hairdresserImage.load(url)

                if (loggedInUserKey != null) {
                    checkFavorite(response.body()!!.key)
                }
                binding.favoriteButton.setOnClickListener {
                    changeFavorite(response.body()!!.key)
                }
            }

            override fun onFailure(call: Call<HairdresserDetails>, t: Throwable) {
                Log.e("Load Time", "onFailure: " + t.message)
            }
        })
        binding.shareButton.setOnClickListener {
            // Text rausholen
            val s = binding.hairdresserName.text.toString() + ", " +
                    binding.hairdresserAddress.text.toString()

            // Share Intent
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Empfehlung geht raus!")
                putExtra(Intent.EXTRA_TEXT, s)
                /*putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.parse("/drawable/fiverr_reshairvation_r1_02_final.png")
                )*/
                type = "*/*"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        binding.resbtn.setOnClickListener {
            findNavController().navigate(R.id.action_friseursalon_to_termindetails,
            Bundle().apply {
                putString("imgLink", url)
                putString("friseurname", binding.hairdresserName.text.toString())
                putString("adresse", binding.hairdresserAddress.text.toString())
                putString("hairsalon_key", requireArguments().getString("hairsalon_key")!!)
            })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setServices(services: ArrayList<String>) {
        val lv = view?.findViewById(R.id.Services) as ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, services)
        lv.adapter = adapter
    }

    private fun checkFavorite(hairdresserKey: String) {
        val userkeyHairdresserkey = FavoriteByUserAndHairdresser(
            listOf(
                FavoriteQueryUserkeyHairdresserkey(
                    loggedInUserKey!!,
                    hairdresserKey
                )
            )
        )
        val retrofitData =
            DbApi.retrofitService.getFavoriteByUserkeyHairdresserkey(userkeyHairdresserkey)
        retrofitData.enqueue(object : Callback<FavoritesList> {
            override fun onResponse(
                call: Call<FavoritesList?>,
                response: Response<FavoritesList?>
            ) {
                if (response.body()!!.paging.size == 1) {
                    binding.favoriteButton.setColorFilter(Color.RED)
                } else {
                    binding.favoriteButton.setColorFilter(Color.WHITE)
                }
            }

            override fun onFailure(call: Call<FavoritesList>, t: Throwable) {
                Log.e("Check Favorite", "onFailure: " + t.message)
            }
        })
    }

    private fun changeFavorite(hairdresserKey: String) {
        val userkeyHairdresserkey = FavoriteByUserAndHairdresser(
            listOf(
                FavoriteQueryUserkeyHairdresserkey(
                    loggedInUserKey!!,
                    hairdresserKey
                )
            )
        )
        val retrofitData =
            DbApi.retrofitService.getFavoriteByUserkeyHairdresserkey(userkeyHairdresserkey)
        retrofitData.enqueue(object : Callback<FavoritesList> {
            override fun onResponse(
                call: Call<FavoritesList?>,
                response: Response<FavoritesList?>
            ) {
                when (response.body()!!.paging.size) {
                    0 -> {
                        val favoriteItem = FavoriteItem(
                            FavoriteDetails(
                                key = null,
                                hairdresser_key = hairdresserKey,
                                user_key = loggedInUserKey!!
                            )
                        )
                        val retrofitData2 = DbApi.retrofitService.storeFavorite(favoriteItem)
                        retrofitData2.enqueue(object : Callback<FavoriteDetails> {
                            override fun onResponse(
                                call: Call<FavoriteDetails?>,
                                response: Response<FavoriteDetails?>
                            ) {
                                Log.i("Favorite", "stored")
                                checkFavorite(hairdresserKey)
                            }

                            override fun onFailure(call: Call<FavoriteDetails>, t: Throwable) {
                                Log.e("Check Favorite", "onFailure: " + t.message)
                            }
                        })
                    }
                    1 -> {
                        val retrofitData2 =
                            DbApi.retrofitService.deleteFavorite(response.body()!!.items[0].key!!)
                        retrofitData2.enqueue(object : Callback<FavoritesKey> {
                            override fun onResponse(
                                call: Call<FavoritesKey?>,
                                response: Response<FavoritesKey?>
                            ) {
                                Log.i("Favorite", "deleted")
                                checkFavorite(hairdresserKey)
                            }

                            override fun onFailure(call: Call<FavoritesKey>, t: Throwable) {
                                Log.e("Check Favorite", "onFailure: " + t.message)
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<FavoritesList>, t: Throwable) {
                Log.e("Check Favorite", "onFailure: " + t.message)
            }
        })
    }
}
