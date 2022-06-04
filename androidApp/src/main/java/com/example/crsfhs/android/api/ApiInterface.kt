package com.example.crsfhs.android.api

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @GET("users/items/{key}")
    fun getUser(@Path("key") userKey: String): Call<UserDetails>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("users/query")
    fun getUsers(): Call<UserList>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("users/items")
    fun storeUser(@Body userItem: UserItem): Call<UserItem>
}