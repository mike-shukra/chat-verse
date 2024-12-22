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
        val request = chain.request().newBuilder()
        accessToken?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(request.build())
    }
}