package com.example.chatverse.data.remote.api

import com.example.chatverse.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainApi {


    @GET("/api/v1/users/me/")
    suspend fun getCurrentUser(): GetCurrentUserProfileDto

}