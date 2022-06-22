package com.example.crsfhs.android.api

data class HairdresserList(
    val items: List<HairdresserDetails>,
    val paging: HairdresserPaging
)

data class HairdresserItem(
    val item: HairdresserDetails
)

data class HairdresserDetails(
    val key: String,
    val active: Boolean,
    val name: String,
    val address: HairdresserAddress,
    val openings: HairdresserOpenings,
    val phone: String,
    val services: HairdresserServices
)

data class HairdresserAddress(
    val city: String,
    val number: String,
    val postcode: String,
    val street: String
)

data class HairdresserOpenings(
    val Di: String,
    val Do: String,
    val Fr: String,
    val Mi: String,
    val Mo: String,
    val Sa: String,
    val So: String
)

data class HairdresserServices(
    val female: List<HairdresserFemale>?,
    val male: List<HairdresserMale>?
)

data class HairdresserMale(
    val name: String,
    val price: String,
    val time: Int
)

data class HairdresserFemale(
    val name: String,
    val price: String,
    val time: Int
)

data class HairdresserPaging(
    val size: Int
)
