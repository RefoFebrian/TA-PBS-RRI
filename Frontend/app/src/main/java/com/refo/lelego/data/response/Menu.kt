package com.refo.lelego.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("nama_menu")
    val namaMenu: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("tersedia")
    val tersedia: Boolean? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("warungId")
    val warungId: Int? = null
) : Parcelable
