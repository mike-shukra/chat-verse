package com.example.chatverse.di

import android.content.SharedPreferences
import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.remote.AuthInterceptor
import com.example.chatverse.data.remote.TokenAuthenticator
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.remote.api.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://plannerok.ru/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(@AuthRetrofit authOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainRetrofit(@MainRetrofit mainOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(mainOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenManager(sharedPreferences: SharedPreferences): TokenManager {
        return TokenManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        tokenManager: TokenManager,
        @AuthRetrofit authRetrofit: Retrofit
    ): TokenAuthenticator {
        val authApi = authRetrofit.create(AuthApi::class.java)
        return TokenAuthenticator(tokenManager, authApi)
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthApi(@AuthRetrofit authRetrofit: Retrofit): AuthApi {
        return authRetrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideMainApi(@MainRetrofit mainRetrofit: Retrofit): MainApi {
        return mainRetrofit.create(MainApi::class.java)
    }
}
