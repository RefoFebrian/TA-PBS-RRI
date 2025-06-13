package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("error")
    val error: Int? = 1,
    @field:SerializedName("message")
    val message: String
)