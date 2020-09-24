package me.rytek.shoppinglist.http

import me.rytek.shoppinglist.http.Request.CreateItemRequest
import me.rytek.shoppinglist.http.Request.CreateListRequest
import me.rytek.shoppinglist.http.Request.LoginRequest
import me.rytek.shoppinglist.http.Request.RegisterRequest
import me.rytek.shoppinglist.http.Responses.LoginResponse
import me.rytek.shoppinglist.http.Responses.MeResponse
import me.rytek.shoppinglist.model.Family
import me.rytek.shoppinglist.model.ShoppingList
import me.rytek.shoppinglist.model.User
import retrofit2.http.*

const val domain = "http://192.168.1.100:3000/"
const val authEndpoint = "auth/"
const val shoppingListEndpoint = "shopping-list/"
const val familyEndpoint = "family/"


interface ShoppingListService {
    @POST(authEndpoint + "login")
    suspend fun Login(@Body user: LoginRequest): LoginResponse

    @GET(authEndpoint + "me")
    suspend fun Me(): MeResponse

    @POST(authEndpoint + "Register")
    suspend fun Register(user: RegisterRequest): User

    @GET(familyEndpoint + "user")
    suspend fun GetUsersFamily(): List<Family>

    @GET("${shoppingListEndpoint}{shoppingListId}")
    suspend fun GetList(@Path("shoppingListId") shoppingListId: Long): ShoppingList

    @POST(shoppingListEndpoint)
    suspend fun CreateList(@Body list: CreateListRequest): ShoppingList

    @POST("$shoppingListEndpoint{shoppingListId}/add")
    suspend fun CreateItem(@Path("shoppingListId") shoppingListId: Long, @Body item: CreateItemRequest): ShoppingList

    @PUT("$shoppingListEndpoint{shoppingListId}/markoff/{itemId}")
    suspend fun MarkOffItem(@Path("shoppingListId") shoppingListId: Long, @Path("itemId") itemId: Long): ShoppingList
}