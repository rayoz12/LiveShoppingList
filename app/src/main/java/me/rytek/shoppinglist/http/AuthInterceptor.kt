package me.rytek.shoppinglist.http

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        var token = ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        req = req.newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(req)
    }
}