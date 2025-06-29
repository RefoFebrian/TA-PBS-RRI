package com.refo.lelego.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class OrderResponse(
    @field:SerializedName("metadata")
    val metadata: Metadata? = null,

    @field:SerializedName("data")
    val data: OrderResponseData? = null
)

@Parcelize
data class OrderResponseData(
    @field:SerializedName("pesananId")
    val pesananId: String? = null,
    @field:SerializedName("transactionId")
    val transactionId: String? = null,
    @field:SerializedName("total_harga")
    val totalHarga: Int? = null
) : Parcelable
