package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Hasil (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("hasil") var hasil : String? = null,
    @SerializedName("pict") var pict : String? = null,
    @SerializedName("solusi") var solusi : String? = null,
    @SerializedName("array_hasil") var array_hasil : ArrayList<Int>? = null

)