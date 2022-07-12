package com.example.crsfhs.android.api

import com.squareup.moshi.Json

data class ReservationsList(
    val items: List<ReservationDetails>,
    val paging: ReservationsPaging
)

data class ReservationItem(
    val item: ReservationDetails
)

data class ReservationDetails(
    val appointment: ReservationAppointment,
    val hairdresser_key: String,
    val key: String?,
    val user_key: String
)

data class ReservationAppointment(
    val date: String,
    val status: String,
    val time_from: String,
    val time_to: String,
    val service: String?
)

data class ReservationsPaging(
    val size: Int
)

data class ReservationByUser(
    val query: List<ReservationQueryUser>
)

data class ReservationQueryUser(
    val user_key: String
)

data class ReservationByHairdresser(
    val query: List<ReservationQueryHairdresser>
)

data class ReservationQueryHairdresser(
    val hairdresser_key: String
)

data class Status(
    val key: String?,
    val set: SetStatus
)

data class ReservationBySalon(
    val query: List<CheckApt>
)
data class CheckApt(
    val hairdresser_key: String,
    @Json(name = "appointment.date")val date: String,
    @Json(name = "appointment.status")val status: String,
    @Json(name = "appointment.time_from")val time_from: String
)

data class SetStatus(
    @Json(name = "appointment.status") val appointmentStatus: String
)
