package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class Riwayat (
    @SerializedName("id" ) var id : String? = null,
    @SerializedName("user_id" ) var user_id : String? = null,
    @SerializedName("hasil"  ) var hasil  : String? = null,
    @SerializedName("pict"  ) var pict  : String? = null,
    @SerializedName("solusi"  ) var solusi  : String? = null,
    @SerializedName("created_at"  ) var created_at  : String? = null
)