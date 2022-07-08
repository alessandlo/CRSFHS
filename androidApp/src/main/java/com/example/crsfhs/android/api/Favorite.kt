package com.example.crsfhs.android.api

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
    val query: List<FavoriteQuery>
)

data class FavoriteQuery(
    val user_key: String
)
