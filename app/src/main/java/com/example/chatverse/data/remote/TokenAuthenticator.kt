package com.example.chatverse.data.remote

import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.remote.dto.RefreshTokenDto
import com.example.chatverse.data.remote.dto.TokenDto
import com.example.chatverse.di.AuthRetrofit
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import android.util.Log

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @AuthRetrofit private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "authenticate triggered for URL: ${response.request.url}")
        val currentRefreshToken = tokenManager.getRefreshToken()

        if (currentRefreshToken == null) {
            Log.w("TokenAuthenticator", "No refresh token available. Cannot refresh.")
            // Инициировать выход пользователя, если это не запрос на логин/регистрацию
            return null // Не удается аутентифицировать
        }

        // Синхронизация, чтобы избежать нескольких одновременных попыток обновления токена
        synchronized(this) {
            // Важно: Проверяем, не был ли токен уже обновлен другим запросом,
            // пока этот ждал synchronized. Сравниваем токен, с которым исходный запрос упал,
            // с текущим токеном в TokenManager. Если они отличаются, значит токен уже обновлен.
            val tokenInFailedRequest = response.request.header("Authorization")?.substringAfter("Bearer ")
            val currentAccessTokenInManager = tokenManager.getAccessToken()

            if (tokenInFailedRequest != null && currentAccessTokenInManager != null && tokenInFailedRequest != currentAccessTokenInManager) {
                Log.d("TokenAuthenticator", "Access token was already refreshed by another thread. Retrying with new token: $currentAccessTokenInManager")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessTokenInManager")
                    .build()
            }

            // Также важно проверить, не изменился ли refresh токен, пока мы ждали.
            // Если refresh токен, с которым мы собираемся идти на сервер, отличается от того,
            // что сейчас в TokenManager (возможно, другой поток его уже обновил и получил новый refresh),
            // то текущая попытка обновления может быть уже неактуальна или приведет к ошибке.
            // Однако, если сервер выдает новый refresh токен при каждом обновлении,
            // то использовать currentRefreshToken, захваченный в начале метода, может быть правильным,
            // чтобы избежать использования refresh токена, который еще не был использован в связке с текущим access токеном.
            // Этот момент зависит от логики вашего бэкенда. Пока оставим как есть (используем currentRefreshToken).


            Log.d("TokenAuthenticator", "Attempting to refresh token.")
            val newTokens: TokenDto? = runBlocking {
                try {
                    val refreshedTokenDto = authApi.refreshToken(RefreshTokenDto(currentRefreshToken))
                    // Так как refreshToken возвращает TokenDto напрямую, мы можем его использовать.
                    // Предполагается, что TokenDto содержит поля accessToken и refreshToken.
                    // Если ваш TokenDto называется иначе или поля другие, скорректируйте.
                    tokenManager.saveAccessToken(refreshedTokenDto.accessToken)
                    tokenManager.saveRefreshToken(refreshedTokenDto.refreshToken) // Важно обновлять и refresh токен, если он меняется
                    Log.i("TokenAuthenticator", "Token refreshed successfully. New access token: ${refreshedTokenDto.accessToken}")
                    refreshedTokenDto // Возвращаем полученные токены
                } catch (e: Exception) {
                    // Это может быть HttpException (например, 401, если refresh-токен невалиден),
                    // IOException (проблемы с сетью), или другая ошибка.
                    Log.e("TokenAuthenticator", "Exception during token refresh", e)
                    tokenManager.clearTokens() // Очищаем токены, так как обновление не удалось
                    // Инициировать выход пользователя
                    null // Возвращаем null при неудаче
                }
            }

            return if (newTokens != null) {
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            } else {
                Log.w("TokenAuthenticator", "Failed to refresh token. Original request will fail.")
                null // Не удалось обновить токен, оригинальный запрос провалится
            }
        }
    }
}