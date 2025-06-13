package com.refo.lelego.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.refo.lelego.data.pref.UserPreferences
import com.refo.lelego.data.response.ErrorResponse
import com.refo.lelego.data.response.RegisterRequest
import com.refo.lelego.data.response.RegisterResponse
import com.refo.lelego.data.retrofit.ApiService
import retrofit2.HttpException

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    fun signup(
        email: String,
        username: String,
        password: String,
        role: String
    ): LiveData<ResultAnalyze<RegisterResponse>> = liveData {
        emit(ResultAnalyze.Loading)
        try {
            val registerRequest = RegisterRequest(email, username, password, role)
            val response = apiService.register(registerRequest)
            emit(ResultAnalyze.Success(response))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultAnalyze.Error(body.message))
        }
    }

    companion object {
        private var INSTANCE: UserRepository? = null

        fun clearInstance() {
            INSTANCE = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService, userPreferences)
            }.also { INSTANCE = it }
    }
}