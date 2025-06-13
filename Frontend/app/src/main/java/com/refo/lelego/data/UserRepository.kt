package com.refo.lelego.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.refo.lelego.data.pref.UserPreferences
import com.refo.lelego.data.response.FullErrorResponse
import com.refo.lelego.data.response.RegisterRequest
import com.refo.lelego.data.response.RegisterResponse
import com.refo.lelego.data.retrofit.ApiService
import retrofit2.HttpException
import android.util.Log

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
            val httpCode = e.code()
            var specificMessage: String? = null

            try {
                val errorJsonString = e.response()?.errorBody()?.string()
                if (errorJsonString != null) {
                    Log.d("UserRepository", "Error JSON from HttpException: $errorJsonString")
                    val fullErrorResponse = Gson().fromJson(errorJsonString, FullErrorResponse::class.java)

                    if (fullErrorResponse?.metadata != null) {
                        specificMessage = fullErrorResponse.metadata.message
                        if (specificMessage.isNullOrEmpty() && fullErrorResponse.metadata.error != null) {
                            specificMessage = "Error code from JSON metadata: ${fullErrorResponse.metadata.error}"
                        }
                    } else {
                        Log.w("UserRepository", "Parsed FullErrorResponse is null or its metadata is null from error JSON.")
                    }
                } else {
                    Log.w("UserRepository", "HttpException error body is null for code $httpCode.")
                }
            } catch (jsonException: Exception) {
                Log.e("UserRepository", "Failed to parse error JSON for code $httpCode: ${jsonException.message}", jsonException)
            }

            val finalErrorMessage = if (!specificMessage.isNullOrBlank()) {
                specificMessage
            } else {
                val genericHttpError = when (httpCode) {
                    409 -> "Conflict"
                    in 500..599 -> "Server Error"
                    else -> e.message().takeIf { !it.isNullOrBlank() }
                }
                "Error $httpCode: ${genericHttpError ?: "Please try again."}"
            }
            emit(ResultAnalyze.Error(finalErrorMessage))

        } catch (e: Exception) {
            Log.e("UserRepository", "Signup failed with generic exception: ${e.javaClass.simpleName}", e)
            emit(ResultAnalyze.Error(e.message ?: "An unexpected error occurred. Please check your connection."))
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