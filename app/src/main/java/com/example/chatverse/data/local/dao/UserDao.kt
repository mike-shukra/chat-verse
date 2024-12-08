package com.example.chatverse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.chatverse.data.local.model.UserProfileEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfileEntity)

    @Query("SELECT * FROM userprofile WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserProfileEntity?

    @Query("DELETE FROM userprofile")
    suspend fun clearUsers()
}