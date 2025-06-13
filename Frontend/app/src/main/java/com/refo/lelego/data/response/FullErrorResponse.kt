package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class FullErrorResponse(
    @field:SerializedName("metadata")
    val metadata: ErrorMetadata?,

    @field:SerializedName("data")
    val data: Any? = null
)