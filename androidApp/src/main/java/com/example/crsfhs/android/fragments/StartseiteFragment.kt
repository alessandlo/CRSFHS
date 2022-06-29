package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crsfhs.android.activities.CustomAdapter
import com.example.crsfhs.android.api.DbApi
import com.example.crsfhs.android.api.HairdresserDetails
import com.example.crsfhs.android.api.HairdresserList
import com.example.crsfhs.android.databinding.FragmentStartseiteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartseiteFragment : Fragment() {
    private lateinit var binding: FragmentStartseiteBinding
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartseiteBinding.inflate(inflater, container, false)

        val hairdresser: ArrayList<HairdresserDetails> = getAllHairdressers()

        val searchbar = binding.searchbar.searchbarText
        searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }
        })

        return binding.root
    }

    private fun getAllHairdressers(): ArrayList<HairdresserDetails> {
        val retrofitData = DbApi.retrofitService.getHairdressers()
        val list: ArrayList<HairdresserDetails> = ArrayList()

        retrofitData.enqueue(object : Callback<HairdresserList> {
            override fun onResponse(
                call: Call<HairdresserList>,
                response: Response<HairdresserList>
            ) {
                println(response.message())
                response.body()!!.items.forEach {
                    list.add(it)
                }
                list.let {
                    val recyclerView = binding.rv
                    recyclerView.layoutManager = LinearLayoutManager(activity)

                    adapter = CustomAdapter(it)

                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<HairdresserList>, t: Throwable) {
                Log.e("Hairdresser", "onFailure: " + t.message)
            }
        })
        return list
    }
}
