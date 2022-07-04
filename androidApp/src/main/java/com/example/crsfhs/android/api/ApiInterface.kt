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
    fun storeUser(@Body userItem: UserItem): Call<UserDetails>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("users/query")
    fun checkUser(@Body userCheck: UserCheck): Call<UserList>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("reservations/query")
    fun getReservartionsByUserkey(@Body reservationByUser: ReservationByUser): Call<ReservationsList>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @GET("hairdressers/items/{key}")
    fun getHairdresser(@Path("key") hairdresserKey: String): Call<HairdresserDetails>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("hairdressers/query")
    fun getHairdressers(): Call<HairdresserList>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @GET("reservations/items/{key},{time}")
    fun validateAppointment(@Path("key") salonKey: String, @Path("time")time: String): Call<ReservationsDetails>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @POST("reservations/query")
    fun saveAppointment(@Body reservationsItem: ReservationsItem): Call<ReservationsDetails>

    @Headers(
        "X-API-Key:a0a1f9b4_2cTPcAgSHEb8ZtmiRBHx5TbRPyniXU2R",
        "Content-Type: application/json"
    )
    @GET("hairdressers/items/{key},{openings}")
    fun getTimes(@Path("key") salonKey: String, @Path ("openings") date : String): Call<HairdresserOpeningsTime>
}