package com.example.crsfhs.android.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crsfhs.android.R
import com.example.crsfhs.android.adapter.CustomAdapter
import com.example.crsfhs.android.api.*
import com.example.crsfhs.android.databinding.FragmentStartseiteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class StartseiteFragment : Fragment(), LocationListener{
    private lateinit var binding: FragmentStartseiteBinding
    private lateinit var activity: Activity
    private lateinit var locationManager: LocationManager
    private var location: Location = Location("empty").apply { latitude = 0.0;longitude = 0.0 }
    private lateinit var address: Address
    private lateinit var geocoder: Geocoder
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartseiteBinding.inflate(inflater, container, false)
        activity = requireActivity()
        geocoder = Geocoder(requireContext())
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasLocationPermission: () -> Boolean = {ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED}

        if(!hasLocationPermission()) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val hairdresser: ArrayList<HairdresserDetails> = ArrayList()
        val recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CustomAdapter(hairdresser) { key ->
            val bundle = bundleOf(
                "hairsalon_key" to key
            )
            binding.root.findNavController().navigate(R.id.action_fragment_startseite_to_fragment_friseur, bundle)
        }
        recyclerView.adapter = adapter
        addAllHairdressers(adapter)

        if (hasLocationPermission()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000 * 60,
                0F,
                this
            )
            val location_last_known = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            println(locationManager.getProviders(true))
            if(location_last_known != null){
                setAdapterCity(location_last_known)
            }
        }
        //binding.searchbar.searchbarLayout.minHeight = binding.searchbar.searchbarLayout.height + 200

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

    private fun addAllHairdressers(adapter: CustomAdapter){
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
                adapter.addItems(list)
            }

            override fun onFailure(call: Call<HairdresserList>, t: Throwable) {
                Log.e("Hairdresser", "onFailure: " + t.message)
            }
        })
    }

    private fun setAdapterCity(location: Location){
        var addresses: List<Address>? = null
        repeat(10) {
            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        this.location = location
        if (addresses != null){
            address = addresses!![0]
            adapter.setCity(address.locality)
        }
    }

    override fun onLocationChanged(location: Location) {
        setAdapterCity(location)
        this.locationManager.removeUpdates(this)
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }
}
