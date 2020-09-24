package me.rytek.shoppinglist.repository

import org.koin.dsl.module

val repositoryModule = module {

    single { AuthRepository(get()) }

}