package me.rytek.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import me.rytek.shoppinglist.http.AuthInterceptor
import me.rytek.shoppinglist.http.Request.CreateItemRequest
import me.rytek.shoppinglist.http.Request.CreateListRequest
import me.rytek.shoppinglist.http.Request.LoginRequest
import me.rytek.shoppinglist.http.ShoppingListService
import me.rytek.shoppinglist.model.Family
import me.rytek.shoppinglist.model.Item
import me.rytek.shoppinglist.model.ShoppingList
import me.rytek.shoppinglist.model.User
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class ShoppingListViewModel() : ViewModel(), KoinComponent {
    // TODO: Implement the ViewModel

    val service: ShoppingListService by inject()

    // UI State
    val isLoadingItemData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    // Data
    // Create a LiveData with a String
    val isLoggedIn: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val user: MutableLiveData<User?> by lazy {
        MutableLiveData<User?>()
    }

    val fullName: LiveData<String> = Transformations.map(user) { user -> run {
        if (user == null) {
            return@run null
        }
        return@run "${user.firstName} ${user.lastName}"
    }}

    val userFamilies: MutableLiveData<List<Family>> by lazy {
        MutableLiveData<List<Family>>(ArrayList())
    }

    val selectedFamily: MutableLiveData<Family> by lazy {
        MutableLiveData<Family>()
    }

    val selectedList: MutableLiveData<ShoppingList> by lazy {
        MutableLiveData<ShoppingList>()
    }

    val markedOffItems: LiveData<List<Item>> = Transformations.map(selectedList) {list -> run {
        list.items.filter { it.markedOff }
    }}

    val items: LiveData<List<Item>> = Transformations.map(selectedList) {list -> run {
        list.items.filter { !it.markedOff }
    }}

    suspend fun me(): User {
        val response = service.Me()
        // Return user
        return response.user
    }

    suspend fun getUserFamilies(): List<Family> {
        val response = service.GetUsersFamily()

        //Set Data Vals
        userFamilies.postValue(response)
        isLoggedIn.postValue(true)
        // Return user
        return response
    }

    suspend fun login (username: String, password: String): User? {
        return try {
            val response = service.Login(LoginRequest(username, password))
            AuthInterceptor.token = response.token
            val userInfo = me()
            //Set Data Vals
            user.postValue(userInfo)
            // Return user
            userInfo
        } catch (e: Exception) {
            Log.e(ShoppingListService::class::simpleName.get(), e.toString())
            null
        }
    }

    /**
     * Set the slected family for the user
     *
     * @param family The family
     * @param shouldPostValue this should be true when not calling from the UI thread. Otherwise omit this value
     */
    fun setFamily(family: Family, shouldPostValue: Boolean = false) {
        if (shouldPostValue) {
            selectedFamily.postValue(family)
        }
        else {
            selectedFamily.value = family
        }
    }

    fun setShoppingList(family: ShoppingList, shouldPostValue: Boolean = false) {
        if (shouldPostValue) {
            selectedList.postValue(family)
        }
        else {
            selectedList.value = family
        }
    }

    suspend fun refreshSelectedList() {
        if (selectedList.value != null) {
            selectedList.postValue(service.GetList(selectedList.value!!.id))
        }
    }

    suspend fun markOffItem(item: Item) {
        if (selectedList.value != null) {
            val list = service.MarkOffItem(selectedList.value!!.id, item.id)
            this.selectedList.postValue(list)
//            item.markedOff = !item.markedOff
//            this.selectedList.value = this.selectedList.value
        }
    }

    suspend fun createList(name: String, familyId: Long) {
        val list = service.CreateList(CreateListRequest(name, familyId))
        this.setFamily(list.family!!, true)
    }

    suspend fun createItem(name: String, quantity: Int, comments: String) {
        val list = service.CreateItem(selectedList.value!!.id, CreateItemRequest(name, quantity, comments))
        this.selectedList.postValue(list)
    }

}
