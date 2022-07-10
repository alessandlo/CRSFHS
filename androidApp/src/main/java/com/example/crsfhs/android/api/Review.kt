package com.example.crsfhs.android.api

data class ReviewList(
    val items: List<ReviewDetails>,
    val paging: ReviewPaging
)

data class ReviewItem(
    val item: List<ReviewDetails>
)

data class ReviewDetails(
    val date: String,
    val description: String,
    val hairdresser_key: String,
    val key: String,
    val rating: String,
    val user_key: String
)

data class ReviewPaging(
    val size: Int
)

data class ReviewByUser(
    val query: List<ReviewQuery>
)

data class ReviewQuery(
    val user_key: String
)
