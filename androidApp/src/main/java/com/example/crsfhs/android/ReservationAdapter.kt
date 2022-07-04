package com.example.crsfhs.android

import android.graphics.Color
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
                "${reservationsDetails.reservationsDetails.appointment.date} · " +
                        "${reservationsDetails.reservationsDetails.appointment.time_from}-" +
                        reservationsDetails.reservationsDetails.appointment.time_to

            image.load(reservationsDetails.hairdresserDetails.img.icon)

            if (reservationsDetails.reservationsDetails.appointment.status == "aktiv") {
                status.text = "✓ aktiv"
                //status.setTextColor(Color.GREEN)
            } else if (reservationsDetails.reservationsDetails.appointment.status == "vergangen") {
                status.text = "✓ vergangen"
                //status.setTextColor(Color.BLUE)
            } else if (reservationsDetails.reservationsDetails.appointment.status == "storniert") {
                status.text = "✗ storniert"
                //status.setTextColor(Color.RED)
            }

            name.text = reservationsDetails.hairdresserDetails.name
            address.text = hairdresserAddress
            //status.text = reservationsDetails.reservationsDetails.appointment.status
            appointment.text = appointmentReservation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reservation_item, parent, false)
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
