package com.wolt.android.demo.data.search

import com.google.gson.annotations.SerializedName

class SearchResponse {
    data class Id(
        @SerializedName("\$oid")
        val id: String
    )

    data class Name(
        val lang: String,
        val value: String
    )

    data class ShortDescription(
        val lang: String,
        val value: String
    )

    data class Result(
        val id: Id,
        val name: List<Name>,
        @SerializedName("short_description")
        val shortDescription: List<ShortDescription>,
        @SerializedName("listimage")
        val imageUrl: String
    )

    val results: List<Result> = emptyList()
    val status: String = ""
}