package com.example.crsfhs.android

import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.crsfhs.android.api.Appointment

class ReservationAdapter(
    private val appointmentList: ArrayList<Appointment>,
    private val listener: (Appointment) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

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
                "${reservationsDetails.reservationDetails.appointment.date} · " +
                        "${reservationsDetails.reservationDetails.appointment.time_from}-" +
                        reservationsDetails.reservationDetails.appointment.time_to

            image.load(reservationsDetails.hairdresserDetails.img.icon)

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

            name.text = reservationsDetails.hairdresserDetails.name
            address.text = hairdresserAddress
            appointment.text = appointmentReservation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val appointmentItem = appointmentList[position]
        holder.bindData(appointmentItem)
        holder.itemView.setOnClickListener { listener(appointmentItem) }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}
