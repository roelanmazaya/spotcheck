package com.example.spotcheck.models

data class DataHasil(
    val id: Int,
    val hasil: String,
    val pict: String,
    val pict2: String,
    val pict3: String,
    val solusi: String,
    val arrayHasil: ArrayList<Int>
)

