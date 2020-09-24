package me.rytek.shoppinglist.http.Request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateListRequest (val name: String, val family: Long)

@JsonClass(generateAdapter = true)
data class CreateItemRequest (val name: String, val quantity: Int, val comments: String)