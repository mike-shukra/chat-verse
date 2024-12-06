package com.example.chatverse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.chatverse.data.local.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clearUsers()
}