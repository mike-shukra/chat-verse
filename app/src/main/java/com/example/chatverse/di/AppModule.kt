package com.example.chatverse.di

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.local.dao.UserDao
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.remote.api.MainApi
import com.example.chatverse.data.repository.UserRepositoryImpl
import com.example.chatverse.domain.repository.UserRepository
import com.example.chatverse.presentation.AuthEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthEventChannel(): MutableSharedFlow<AuthEvent> {
        // replay = 0, чтобы новые подписчики не получали старые события, если это не нужно
        // extraBufferCapacity = 1, чтобы tryEmit не терял событие, если подписчика еще нет, но он вот-вот появится
        return MutableSharedFlow(replay = 0, extraBufferCapacity = 1)
    }

    /*
     * CoilModule
     */
    @Provides
    @Singleton
    fun provideCoilImageLoader(@ApplicationContext context: Context, @MainRetrofit okHttpClient: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideUserRepository(
        @AuthRetrofit authApi: AuthApi,
        @MainRetrofit mainApi: MainApi,
        tokenManager: TokenManager, userDao: UserDao): UserRepository {
        return UserRepositoryImpl(authApi, mainApi, tokenManager, userDao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

}