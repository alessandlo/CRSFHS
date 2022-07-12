package com.example.crsfhs.android.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crsfhs.android.R
import com.example.crsfhs.android.api.AppointmentHairdresser

class ReservationAdapterHairdresser(
    private val appointmentList: ArrayList<AppointmentHairdresser>
) : RecyclerView.Adapter<ReservationAdapterHairdresser.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(reservationsDetails: AppointmentHairdresser) {
            val customer = itemView.findViewById<TextView>(R.id.customerName)
            val service = itemView.findViewById<TextView>(R.id.reservationService)
            val status = itemView.findViewById<TextView>(R.id.reservationStatus)
            val appointment = itemView.findViewById<TextView>(R.id.reservationAppointment)

            val appointmentReservation =
                "${reservationsDetails.reservationDetails.appointment.date} · " +
                        "${reservationsDetails.reservationDetails.appointment.time_from}-" +
                        reservationsDetails.reservationDetails.appointment.time_to

            when (reservationsDetails.reservationDetails.appointment.status) {
                "aktiv" -> {
                    status.text = "✓ aktiv"
                    status.background.colorFilter =
                        PorterDuffColorFilter(Color.parseColor("#FF009688"), PorterDuff.Mode.SRC_IN)
                    status.setTextColor(Color.parseColor("#FF009688"))
                }
                "vergangen" -> {
                    status.text = "✓ vergangen"
                    status.background.colorFilter =
                        PorterDuffColorFilter(Color.parseColor("#FF2196F3"), PorterDuff.Mode.SRC_IN)
                    status.setTextColor(Color.parseColor("#FF2196F3"))
                }
                "storniert" -> {
                    status.text = "✗ storniert"
                    status.background.colorFilter =
                        PorterDuffColorFilter(Color.parseColor("#FFF44336"), PorterDuff.Mode.SRC_IN)
                    status.setTextColor(Color.parseColor("#FFF44336"))
                }
            }

            customer.text = reservationsDetails.userDetails.firstname + reservationsDetails.userDetails.lastname
            service.text = reservationsDetails.reservationDetails.appointment.service
            appointment.text = appointmentReservation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reservation_hairdresser, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val appointmentItem = appointmentList[position]
        holder.bindData(appointmentItem)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}