package me.rytek.shoppinglist.http.Request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest (val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class RegisterRequest (var email: String, var password: String, var firstname: String, var lastname: String)