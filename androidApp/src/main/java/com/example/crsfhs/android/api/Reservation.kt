package com.example.crsfhs.android.api

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
    val key: String,
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
