package com.example.crsfhs.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.api.*

class ReservationAdapter(private val appointmentList: MutableList<Appointment>) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {
    inner class ReservationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(reservationsDetails: Appointment) {
            val image = itemView.findViewById<ImageView>(R.id.hairdresserIcon)
            val name = itemView.findViewById<TextView>(R.id.hairdresserName)
            val address = itemView.findViewById<TextView>(R.id.hairdresserAddress)
            val status = itemView.findViewById<TextView>(R.id.reservationStatus)
            val appointment = itemView.findViewById<TextView>(R.id.reservationAppointment)

            val hairdresserAddress =
                "${reservationsDetails.hairdresserDetails.address.street} " +
                        "${reservationsDetails.hairdresserDetails.address.number}, " +
                        "${reservationsDetails.hairdresserDetails.address.postcode} " +
                        reservationsDetails.hairdresserDetails.address.city

            val appointmentReservation =
                "${reservationsDetails.reservationsDetails.appointment.date} · " +
                        "${reservationsDetails.reservationsDetails.appointment.time_from}-" +
                        reservationsDetails.reservationsDetails.appointment.time_to

            image.load(reservationsDetails.hairdresserDetails.img.icon)
            name.text = reservationsDetails.hairdresserDetails.name
            address.text = hairdresserAddress
            status.text = reservationsDetails.reservationsDetails.appointment.status
            appointment.text = appointmentReservation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reservation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bindData(appointmentList[position])
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}
