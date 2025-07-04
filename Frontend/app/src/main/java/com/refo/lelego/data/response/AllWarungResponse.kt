package com.refo.lelego.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class AllWarungResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: List<WarungDetailData> = emptyList()
)

@Parcelize
data class Penjual(

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("jam_buka")
	val jamBuka: String? = null,

	@field:SerializedName("jam_tutup")
	val jamTutup: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("no_telp")
	val noTelp: String? = null,

	@field:SerializedName("menu")
	val menu: List<Menu?>? = null,

	@field:SerializedName("penjual")
	val penjual: Penjual? = null,

	@field:SerializedName("penjualId")
	val penjualId: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
) : Parcelable
