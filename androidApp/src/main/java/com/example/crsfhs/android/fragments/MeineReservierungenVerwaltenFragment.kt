package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crsfhs.android.databinding.FragmentMeineReservierungenVerwaltenBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MeineReservierungenVerwaltenFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMeineReservierungenVerwaltenBinding
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeineReservierungenVerwaltenBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        setData()

        return binding.root
    }

    fun setData() {
        val address =
            "${arguments?.getString("hairdresser_street")} " +
                    "${arguments?.getString("hairdresser_number")}, " +
                    "${arguments?.getString("hairdresser_postcode")} " +
                    "${arguments?.getString("hairdresser_city")}"

        val appointment =
            "${arguments?.getString("appointment_date")} · " +
                    "${arguments?.getString("appointment_time_from")}-" +
                    "${arguments?.getString("appointment_time_to")}"

        binding.hairdresserName.text = arguments?.getString("hairdresser_name")
        binding.reservationStatus.text = arguments?.getString("appointment_status")
        binding.hairdresserAddress.text = address
        binding.reservationAppointment.text = appointment
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val mark = LatLng(50.1165, 8.6850)
        googleMap.addMarker(
            MarkerOptions().position(mark).title(arguments?.getString("hairdresser_name"))
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 15F))
    }

}
