package me.rytek.shoppinglist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.rytek.shoppinglist.http.Request.LoginRequest
import me.rytek.shoppinglist.http.ShoppingListService
import me.rytek.shoppinglist.model.User
import org.koin.android.ext.android.inject


class AuthRepository(private val shoppingListService: ShoppingListService) {

    suspend fun Login(loginRequest: LoginRequest) = shoppingListService.Login(loginRequest)
}
