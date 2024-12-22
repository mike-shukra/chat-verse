package com.example.chatverse

import android.app.Application
import android.util.Log
import com.example.chatverse.data.AppConstants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatVerseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(AppConstants.LOG_TAG, "Application is initialized")
    }
}
