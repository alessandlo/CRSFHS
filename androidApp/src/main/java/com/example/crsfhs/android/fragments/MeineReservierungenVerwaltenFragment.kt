package com.example.crsfhs.android.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.DbApi
import com.example.crsfhs.android.api.SetStatus
import com.example.crsfhs.android.api.Status
import com.example.crsfhs.android.databinding.FragmentMeineReservierungenVerwaltenBinding
import com.example.crsfhs.android.services.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeineReservierungenVerwaltenFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMeineReservierungenVerwaltenBinding
    private lateinit var coordinates: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeineReservierungenVerwaltenBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.cancelButton.setOnClickListener{
            cancelAppointment(arguments!!.getString("reservation_key")!!)
        }

        setData()

        return binding.root
    }

    fun setData() {
        coordinates = Location.getLocationFromAddress(
            context!!,
            arguments!!.getString("hairdresser_street") + " " +
                    arguments!!.getString("hairdresser_number") + " " +
                    arguments!!.getString("hairdresser_postcode") + " " +
                    arguments!!.getString("hairdresser_city")
        )!!

        val address =
            "${arguments!!.getString("hairdresser_street")} " +
                    "${arguments!!.getString("hairdresser_number")}, " +
                    "${arguments!!.getString("hairdresser_postcode")} " +
                    "${arguments!!.getString("hairdresser_city")}"

        val appointment =
            "${arguments!!.getString("appointment_date")} · " +
                    "${arguments!!.getString("appointment_time_from")}-" +
                    "${arguments!!.getString("appointment_time_to")}"

        when (arguments!!.getString("appointment_status")) {
            "aktiv" -> {
                binding.reservationStatus.text = "✓ aktiv"
                binding.reservationStatus.background.colorFilter =
                    BlendModeColorFilter(Color.parseColor("#FF009688"), BlendMode.SRC_IN)
                binding.reservationStatus.setTextColor(Color.parseColor("#FF009688"))
            }
            "vergangen" -> {
                binding.reservationStatus.text = "✓ vergangen"
                binding.reservationStatus.background.colorFilter =
                    BlendModeColorFilter(Color.parseColor("#FF2196F3"), BlendMode.SRC_IN)
                binding.reservationStatus.setTextColor(Color.parseColor("#FF2196F3"))
                binding.cancelButton.visibility = View.GONE
                binding.seperator.visibility = View.GONE
            }
            "storniert" -> {
                binding.reservationStatus.text = "✗ storniert"
                binding.reservationStatus.background.colorFilter =
                    BlendModeColorFilter(Color.parseColor("#FFF44336"), BlendMode.SRC_IN)
                binding.reservationStatus.setTextColor(Color.parseColor("#FFF44336"))
                binding.cancelButton.visibility = View.GONE
                binding.seperator.visibility = View.GONE
            }
        }

        binding.hairdresserName.text = arguments!!.getString("hairdresser_name")
        //binding.reservationStatus.text = arguments?.getString("appointment_status")
        binding.hairdresserAddress.text = address
        binding.reservationAppointment.text = appointment
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val mark = coordinates
        googleMap.addMarker(
            MarkerOptions().position(mark).title(arguments!!.getString("hairdresser_name"))
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 15F))
    }

    private fun cancelAppointment(key: String){

        MaterialAlertDialogBuilder(context!!)
            .setTitle(resources.getString(R.string.cancelAppointment))
            .setMessage(resources.getString(R.string.cancelAppointment_text))
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                val retrofitData = DbApi.retrofitService.changeStatus(key, Status(null, SetStatus("storniert")))

                retrofitData.enqueue(object : Callback<Status?> {
                    override fun onResponse(
                        call: Call<Status?>,
                        response: Response<Status?>
                    ) {
                        binding.root.findNavController()
                            .navigate(R.id.action_fragment_meine_reservierungen_verwalten_to_fragment_meine_reservierungen)
                        Log.i("Status", "storniert")
                    }

                    override fun onFailure(call: Call<Status?>, t: Throwable) {
                        Log.e("Status", "onFailure: " + t.message)
                    }
                })
            }
            .show()
    }
}
