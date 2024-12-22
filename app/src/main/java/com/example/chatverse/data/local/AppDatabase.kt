package com.example.chatverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatverse.data.local.dao.UserDao
import com.example.chatverse.data.local.model.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}