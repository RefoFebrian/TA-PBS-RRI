package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @field:SerializedName("metadata")
    val metadata: Metadata? = null,

    @field:SerializedName("data")
    val data: OrderResponseData? = null
)

data class OrderResponseData(
    @field:SerializedName("pesananId")
    val pesananId: String? = null,
    @field:SerializedName("transactionId")
    val transactionId: String? = null,
    @field:SerializedName("total_harga")
    val totalHarga: Int? = null
)
