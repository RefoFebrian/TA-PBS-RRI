package com.refo.lelego.data.retrofit

import com.refo.lelego.data.response.AllWarungResponse
import com.refo.lelego.data.response.LoginRequest
import com.refo.lelego.data.response.LoginResponse
import com.refo.lelego.data.response.RegisterRequest
import com.refo.lelego.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("/api/warung")
    suspend fun getWarungNoPaginate(): AllWarungResponse
}