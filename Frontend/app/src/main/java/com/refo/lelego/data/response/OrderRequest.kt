package com.refo.lelego.data.response

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @field:SerializedName("userId")
    val userId: String,
    @field:SerializedName("warungId")
    val warungId: Int,
    @field:SerializedName("waktu_ambil")
    val waktuAmbil: String,
    @field:SerializedName("items")
    val items: List<Item>
) {
    data class Item(
        @field:SerializedName("menuId")
        val menuId: Int?,
        @field:SerializedName("jumlah")
        val jumlah: Int
    )
}