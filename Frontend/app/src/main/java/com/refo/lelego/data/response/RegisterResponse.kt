package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("metadata")
	val metadata: Metadata? = null,

    @field:SerializedName("data")
	val data: Data? = null
)

data class Metadata(

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("resetTokenExpiry")
	val resetTokenExpiry: Any? = null,

	@field:SerializedName("resetToken")
	val resetToken: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
