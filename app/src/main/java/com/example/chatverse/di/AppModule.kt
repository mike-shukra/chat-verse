package com.example.chatverse.di

import android.content.Context
import android.content.SharedPreferences
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.repository.UserRepositoryImpl
import com.example.chatverse.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideUserRepository(@AuthRetrofit authApi: AuthApi): UserRepository {
        return UserRepositoryImpl(authApi)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

}