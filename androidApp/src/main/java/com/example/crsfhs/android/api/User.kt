package com.example.crsfhs.android.api

data class UserList(
    val items: List<UserDetails>,
    val paging: UserPaging
)

data class UserItem(
    val item: UserDetails,
)

data class UserDetails(
    val key: String?,
    val active: Boolean,
    val username: String,
    val role: String,
    val gender: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String?
)

data class UpdateUserDetailsSet (
    val key: String?,
    val set: UpdateUserDetails
)

data class UpdateUserDetails(
    val firstname: String,
    val lastname: String,
    val phone: String,
    val gender: String,
    val password: String
)


data class UserPaging(
    val size: Int
)

data class UserCheck(
    val query: List<UserQuery>
)

data class UserQuery(
    val username: String
)
