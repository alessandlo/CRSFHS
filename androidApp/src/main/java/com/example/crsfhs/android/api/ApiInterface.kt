package com.example.crsfhs.android.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("x-api-key:4d170add2dd0bba92c8771c1598ca62b4e6b8")
    @GET("users")
    fun getUsers(): Call<List<UserItem>>

    @Headers("x-api-key:4d170add2dd0bba92c8771c1598ca62b4e6b8")
    @POST("users")
    fun storeUser(@Body userItem: UserItem): Call<UserItem>
}