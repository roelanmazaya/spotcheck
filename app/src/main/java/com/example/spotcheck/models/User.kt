package com.example.spotcheck.models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id" ) var id : String? = null,
    @SerializedName("email" ) var email : String? = null,
    @SerializedName("role"  ) var role  : String? = null,
    @SerializedName("image"  ) var image  : String? = null,
    @SerializedName("nama"  ) var nama  : String? = null,
    @SerializedName("usia"  ) var usia  : Int?    = null
)