package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Pertanyaan (
    @SerializedName("id"         ) var id         : Int? = null,
    @SerializedName("pertanyaan" ) var pertanyaan : String? = null
)

data class Pertanyaan_Model (
    var docId      : String? = null,
    var id         : Int? = null,
    var pertanyaan : String? = null
)


data class Pertanyaan_Model_Edit (
    var docId      : String? = null,
    var id         : Int? = null,
    var pertanyaan : String? = null,
    var hasil         : Int? = null,
)

data class JawabanPertanyaan(
    val id: Int,
    val pertanyaan: String,
    var jawaban: Int? = null // Nilai jawaban: 1 untuk Iya, -1 untuk Tidak, null jika belum dijawab
)
