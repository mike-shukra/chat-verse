package com.example.chatverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatverse.data.local.dao.UserDao
import com.example.chatverse.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}