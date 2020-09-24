package me.rytek.shoppinglist.http.Responses

import com.squareup.moshi.JsonClass
import me.rytek.shoppinglist.model.User

@JsonClass(generateAdapter = true)
data class LoginResponse (val token: String)

@JsonClass(generateAdapter = true)
data class MeResponse (val id: Long, val username: String, val user: User)