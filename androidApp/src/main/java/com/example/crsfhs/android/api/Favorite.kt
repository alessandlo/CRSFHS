package com.example.crsfhs.android.api

data class FavoritesKey(
    val key: String,
)

data class FavoritesList(
    val items: List<FavoriteDetails>,
    val paging: FavoritesPaging
)

data class FavoriteItem(
    val item: FavoriteDetails
)

data class FavoriteDetails(
    val key: String?,
    val hairdresser_key: String,
    val user_key: String
)

data class FavoritesPaging(
    val size: Int
)

data class FavoritesByUser(
    val query: List<FavoriteQueryUserkey>
)

data class FavoriteQueryUserkey(
    val user_key: String
)

data class FavoriteByUserAndHairdresser(
    val query: List<FavoriteQueryUserkeyHairdresserkey>
)

data class FavoriteQueryUserkeyHairdresserkey(
    val user_key: String,
    val hairdresser_key: String
)
