package com.refo.lelego.data.retrofit

import com.refo.lelego.data.response.RegisterRequest
import com.refo.lelego.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest // Ganti @Field dengan @Body dan gunakan data class RegisterRequest
    ): RegisterResponse
}