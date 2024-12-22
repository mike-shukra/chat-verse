package com.example.chatverse.data.remote

import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.remote.dto.RefreshTokenDto
import com.example.chatverse.di.AuthRetrofit
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @AuthRetrofit private val authApi: AuthApi // Для обновления токенов
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        val newAccessToken = runBlocking {
            try {
                val refreshResponse = authApi.refreshToken(RefreshTokenDto(refreshToken))
                refreshResponse.also {
                    tokenManager.saveAccessToken(it.accessToken)
                    tokenManager.saveRefreshToken(it.refreshToken)
                }
            } catch (e: Exception) {
                tokenManager.clearTokens()
                null
            }
        } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }
}