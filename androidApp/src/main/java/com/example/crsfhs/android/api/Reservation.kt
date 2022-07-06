package com.example.crsfhs.android.api

import com.squareup.moshi.Json

data class ReservationsList(
    val items: List<ReservationsDetails>,
    val paging: ReservationsPaging
)

data class ReservationsItem(
    val item: ReservationsDetails
)

data class ReservationsDetails(
    val appointment: ReservationsAppointment,
    val hairdresser_key: String,
    val key: String?,
    val user_key: String
)

data class ReservationsAppointment(
    val date: String,
    val status: String,
    val time_from: String,
    val time_to: String
)

data class ReservationsPaging(
    val size: Int
)

data class ReservationByUser(
    val query: List<ReservationQuery>
)

data class ReservationQuery(
    val user_key: String
)

data class Status(
    val key: String?,
    val set: SetStatus
)

data class SetStatus(
    @Json(name = "appointment.status") val appointmentStatus: String
)