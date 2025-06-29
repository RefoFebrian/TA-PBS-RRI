package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: LoginData? = null
)

data class LoginData(
	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("user")
	val user: UserInfo? = null
)

data class UserInfo(
	@field:SerializedName("id")
	val id: String? = null,
	@field:SerializedName("username")
	val username: String? = null
)
