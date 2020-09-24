package me.rytek.shoppinglist.model


import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Item (
    var id: Long,
    var name: String,
    var quantity: Long,
    var comments: String,
    var markedOff: Boolean,
    var isDeleted: Boolean,
    var timeAdded: Date,
    var timeMarkedOff: Date?,
    var timeDeleted: Date?,
    var addedBy: User
)