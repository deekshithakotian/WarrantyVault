package com.example.warrantyvault.common.remote

import com.example.warrantyvault.common.firebase.FcmTokenRequest
import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.common.model.ApiResponse
import com.example.warrantyvault.common.model.PresignedRequest
import com.example.warrantyvault.common.model.PresignedResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface ProductService {

    @POST("products/")
    suspend fun saveProduct(@Body product: Product): Response<ApiResponse<Product>>

    @POST("generate-uri/")
    suspend fun generateUri(@Body fileName: PresignedRequest): PresignedResponse

    @POST("save-fcm-token/")
    suspend fun saveFcmToken(@Body request: FcmTokenRequest): Response<ApiResponse<String>>
}