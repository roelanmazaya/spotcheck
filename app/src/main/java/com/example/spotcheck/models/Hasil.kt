package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Hasil(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("hasil") var hasil: String? = null,
    @SerializedName("pict") var pict: String? = null,
    @SerializedName("pict2") var pict2: String? = null,
    @SerializedName("pict3") var pict3: String? = null,
    @SerializedName("solusi") var solusi: String? = null,
    @SerializedName("array_hasil") var array_hasil: List<Int>? = null

)

data class Penyakit(
    var docId: String? = null,
    var id: Int? = null,
    var hasil: String? = null,
    var pict: String? = null,
    var pict2: String? = null,
    var pict3: String? = null,
    var solusi: String? = null,
    var array_hasil: List<Int>? = null,

    )