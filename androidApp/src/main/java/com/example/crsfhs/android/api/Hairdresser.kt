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
    val img: HairdresserImages,
    val address: HairdresserAddress,
    val openings: HairdresserOpenings,
    val phone: String,
    val rating: String,
    val services: List<HairdresserServices>
)

data class HairdresserImages(
    val icon: String,
    val logo: String,
)

data class HairdresserAddress(
    val city: String,
    val number: String,
    val postcode: String,
    val street: String
)

data class HairdresserOpenings(
    val Di: HairdresserOpeningsTime,
    val Do: HairdresserOpeningsTime,
    val Fr: HairdresserOpeningsTime,
    val Mi: HairdresserOpeningsTime,
    val Mo: HairdresserOpeningsTime,
    val Sa: HairdresserOpeningsTime,
    val So: HairdresserOpeningsTime
)

data class HairdresserServices(
    val name: String,
    val price: String,
    val time: Int,
    val gender: String
)

data class HairdresserPaging(
    val size: Int
)

data class OpeningTimes(
    val query: List<HairdresserQuery>
)

class HairdresserQuery(
    val key: String
)

data class HairdresserOpeningsTime(
    val time_from: String,
    val time_to: String
)
