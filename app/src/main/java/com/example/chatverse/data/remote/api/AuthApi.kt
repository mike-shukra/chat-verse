package com.example.chatverse.data.remote.api

import com.example.chatverse.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body phoneBase: PhoneBaseDto): Result<Unit>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCode: CheckAuthCodeDto): LoginResponseDto

    @POST("/api/v1/users/register/")
    suspend fun registerUser(@Body registerIn: RegisterInDto): TokenDto

    @GET("/api/v1/users/me/")
    suspend fun getCurrentUser(): GetCurrentUserProfileDto
}
