package me.rytek.shoppinglist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShoppingList (var id: Long, var name: String, var isActive: Boolean, var sortOrder: ShoppingListSortOrder, var items: List<Item>, var family: Family? = null)