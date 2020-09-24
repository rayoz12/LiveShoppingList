package me.rytek.shoppinglist

import android.app.Application
import me.rytek.shoppinglist.appModule
import me.rytek.shoppinglist.http.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShoppingListApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        // start Koin!
        startKoin {
            androidLogger()
            androidContext(this@ShoppingListApplication)
            modules(listOf(networkModule, appModule))
        }
    }
}