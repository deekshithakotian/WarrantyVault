package com.example.warrantyvault.common.remote

import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.common.model.ApiResponse
import com.example.warrantyvault.common.model.UserLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

   @POST("auth/login/")
    suspend fun login(@Body userLogin: UserLogin):Response<ApiResponse<UserLogin>>

}