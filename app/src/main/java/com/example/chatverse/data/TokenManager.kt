package com.example.chatverse.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        private const val PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().putString(PREF_ACCESS_TOKEN, accessToken).apply()
        sharedPreferences.edit().putString(PREF_REFRESH_TOKEN, refreshToken).apply()

    }
    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(PREF_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(PREF_ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(PREF_REFRESH_TOKEN, token).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(PREF_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        sharedPreferences.edit()
            .remove(PREF_ACCESS_TOKEN)
            .remove(PREF_REFRESH_TOKEN)
            .apply()
    }
}
