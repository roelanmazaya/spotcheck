package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Pertanyaan (
    @SerializedName("id"         ) var id         : Int? = null,
    @SerializedName("pertanyaan" ) var pertanyaan : String? = null
)