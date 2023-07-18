package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Riwayat (
    @SerializedName("id" ) var id : Int? = null,
    @SerializedName("user_id" ) var user_id : String? = null,
    @SerializedName("hasil"  ) var hasil  : String? = null,
    @SerializedName("pict"  ) var pict  : String? = null,
    @SerializedName("pict2"  ) var pict2  : String? = null,
    @SerializedName("pict3"  ) var pict3  : String? = null,
    @SerializedName("solusi"  ) var solusi  : String? = null,
    @SerializedName("created_at"  ) var created_at  : String? = null
)

data class Riwayat_model (
    var no : Int? = null,
    var id : Int? = null,
    var user_id : String? = null,
    var hasil : String? = null,
    var pict : String? = null,
    var pict2 : String? = null,
    var pict3 : String? = null,
    var solusi : String? = null,
    var created_at : String? = null
)