package me.rytek.shoppinglist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Family (var id: Long, var name: String, var manager: User, var isActive: Boolean, var members: List<User>, var lists: List<ShoppingList>)