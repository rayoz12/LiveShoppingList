package me.rytek.shoppinglist

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // MyViewModel ViewModel
    viewModel { ShoppingListViewModel() }
}