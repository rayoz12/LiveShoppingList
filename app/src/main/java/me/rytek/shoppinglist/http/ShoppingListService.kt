package me.rytek.shoppinglist.http

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

val networkModule = module {
    factory { AuthInterceptor() }
    factory {HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }}
    factory { provideOkHttpClient(get(), get()) }
    factory { provideShoppingListService(get()) }
    single { provideRetrofit(get()) }

}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
    return Retrofit.Builder()
        .baseUrl(domain)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor, logInterceptor: HttpLoggingInterceptor): OkHttpClient {
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(logInterceptor)
        .addInterceptor(authInterceptor)
        .build()
}

fun provideShoppingListService(retrofit: Retrofit): ShoppingListService = retrofit.create(ShoppingListService::class.java)