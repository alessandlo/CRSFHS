package com.example.crsfhs.android.api

data class Appointment(
    val hairdresserDetails: HairdresserDetails,
    val reservationDetails: ReservationDetails
)

data class Review(
    val hairdresserDetails: HairdresserDetails,
    val reviewDetails: ReviewDetails
)