package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class ErrorMetadata(
    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int? = null
)