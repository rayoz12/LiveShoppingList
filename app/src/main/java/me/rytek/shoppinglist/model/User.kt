package me.rytek.shoppinglist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User (var id: Long, var email: String, var firstName: String, var lastName: String, var isActive: Boolean)