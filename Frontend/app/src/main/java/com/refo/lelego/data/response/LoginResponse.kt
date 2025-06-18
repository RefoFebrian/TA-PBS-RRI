package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: LoginData
)

data class LoginData(
	@field:SerializedName("token")
	val token: String
)
