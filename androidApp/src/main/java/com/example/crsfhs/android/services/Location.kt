package com.example.crsfhs.android.services

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

object Location {
    fun getLocationFromAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context, Locale.GERMAN)
        val address: MutableList<Address>?
        var latLng: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 1)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            latLng = LatLng(
                location.getLatitude(),
                location.getLongitude()
            )
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return latLng
    }
}
