package com.wolt.android.demo.data

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    var favorite: Boolean = false
)
