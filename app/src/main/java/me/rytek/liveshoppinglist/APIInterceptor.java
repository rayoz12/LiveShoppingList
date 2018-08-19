package me.rytek.liveshoppinglist;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIInterceptor implements Interceptor {

    private String key = BuildConfig.API_ACCESS_KEY; //provide your key in this file: ~/.gradle/gradle.properties

    public APIInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .addHeader("Authorization", key)
                .build();
        return chain.proceed(newRequest);
    }
}
