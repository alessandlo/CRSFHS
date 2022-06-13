package com.example.crsfhs.android.api

data class UserList(
    val items: List<UserDetails>,
    val paging: Paging
)

data class UserItem(
    val item: UserDetails,
)

data class UserDetails(
    val key: String?,
    val username: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String
)

data class Paging(
    val size: Int
)

data class UserCheck(
    val query: List<UserQuery>
)

data class UserQuery(
    val username: String
)
