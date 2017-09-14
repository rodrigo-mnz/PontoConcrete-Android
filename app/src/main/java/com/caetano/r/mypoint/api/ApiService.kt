package com.caetano.r.mypoint.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("/api/auth/sign_in")
    fun signIn(@Body login: Login): Call<LoginResponse>

    @POST("/api/time_cards/register")
    fun register(@Body register: Register,
                 @Header("access-token") token: String,
                 @Header("client") client: String,
                 @Header("uid") email: String): Call<RegisterResponse>
}