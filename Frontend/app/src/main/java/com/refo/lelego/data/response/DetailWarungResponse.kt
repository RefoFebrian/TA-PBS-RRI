package com.refo.lelego.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DetailWarungResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("data")
	val data: WarungDetailData? = null
)


@Parcelize
data class WarungDetailData(
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
	val menu: List<MenuItem?>? = null,

	@field:SerializedName("penjual")
	val penjual: Penjual? = null,

	@field:SerializedName("penjualId")
	val penjualId: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
) : Parcelable

@Parcelize
data class MenuItem(
	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("nama_menu")
	val namaMenu: String? = null,

	@field:SerializedName("tersedia")
	val tersedia: Boolean? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("warungId")
	val warungId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
) : Parcelable