package com.example.chatverse.data.remote

import com.example.chatverse.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenManager.getAccessToken()
        val requestBuilder = chain.request().newBuilder()
        accessToken?.let { token ->
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}