package com.example.crsfhs.android.api

data class AppointmentUser(
    val hairdresserDetails: HairdresserDetails,
    val reservationDetails: ReservationDetails
)

data class AppointmentHairdresser(
    val userDetails: UserDetails,
    val reservationDetails: ReservationDetails
)

data class Review(
    val hairdresserDetails: HairdresserDetails,
    val reviewDetails: ReviewDetails
)