package com.refo.lelego.data.response

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: String
)