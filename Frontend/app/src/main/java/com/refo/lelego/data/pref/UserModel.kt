package com.refo.lelego.data.pref

data class UserModel(
    val username: String,
    val token: String,
    val password: String,
    val isLogin: Boolean = false,
    val userId: String? = null
)