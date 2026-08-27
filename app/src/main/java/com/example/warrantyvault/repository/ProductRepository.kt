package com.example.warrantyvault.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.work.ListenableWorker
import com.example.warrantyvault.common.firebase.FcmTokenRequest
import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.common.local.ProductDao
import com.example.warrantyvault.common.model.ApiResponse
import com.example.warrantyvault.common.model.ApiState
import com.example.warrantyvault.common.model.PresignedRequest
import com.example.warrantyvault.common.model.PresignedResponse
import com.example.warrantyvault.common.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productSerivce: ProductService
) {

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getUnsyncedProducts(): List<Product> {
        return productDao.getUnSyncedProducts()

    }

    suspend fun getAllProducts(): ApiState<List<Product>>{
        return try {
            val products = productDao.getAllProducts()
            ApiState.Success(products)
        } catch (e: Exception) {
            ApiState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    suspend fun updateSyncStatus(id: Int, markSynced: Int) {
        productDao.updateSyncStatus(id, markSynced)

    }

    suspend fun generate_url(file_name: PresignedRequest): PresignedResponse
    {
        return productSerivce.generateUri(file_name)
    }

    suspend fun uploadImageToS3(
        uploadUrl: String,
        localImagePath: String
    ): Boolean {

        return withContext(Dispatchers.IO) {

            try {

                val file = File(localImagePath)

                val requestBody = file
                    .asRequestBody("image/jpeg".toMediaType())

                val request = Request.Builder()
                    .url(uploadUrl)
                    .put(requestBody)
                    .addHeader("Content-Type", "image/jpeg")
                    .build()

                val client = OkHttpClient()

                val response = client.newCall(request).execute()

                Log.d("S3", "Code: ${response.code}")
                Log.d("S3", "Message: ${response.message}")
                Log.d("S3", "Error: ${response.body?.string()}")

                response.isSuccessful

            } catch (e: Exception) {
                println(e.toString())

                false
            }
        }
    }

    fun getFileName(context: Context, uriString: String): String {
        val uri = Uri.parse(uriString)

        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    return cursor.getString(index)
                }
            }
        }

        return uri.lastPathSegment ?: "image.jpg"
    }


    suspend fun syncToCloud(context: Context)
    {
        val products = getUnsyncedProducts()

        products.forEach { product ->

            try {

                // Upload receipt image
//                val fileReceipt= File(product.receiptImage?:"")
//                val fileWarrantyCard= File(product.warrantyCardImage?:"")
//
//                val preSignedReceipt=generate_url(PresignedRequest(filename = fileReceipt.name))
//                val preSignedWarranty=generate_url(PresignedRequest(filename = fileWarrantyCard.name))

                val receiptFileName = getFileName(context, product.receiptImage ?: "")
                val warrantyFileName = getFileName(context, product.warrantyCardImage ?: "")



                val preSignedReceipt=generate_url(PresignedRequest(fileName = receiptFileName))
                val receiptUploadUrl = preSignedReceipt.uploadUrl
                val receiptImageUrl = preSignedReceipt.imageUrl

                val uploadReceiptResponse=uploadImageToS3(receiptUploadUrl,product.receiptImage?:"")


                val preSignedWarranty=generate_url(PresignedRequest(fileName = warrantyFileName))
                val warrantyUploadUrl = preSignedWarranty.uploadUrl
                val warrantyImageUrl = preSignedWarranty.imageUrl

                val uploadWarrantyResponse=uploadImageToS3(warrantyUploadUrl,product.warrantyCardImage?:"")

                Log.d("URL----",receiptImageUrl)

                if(uploadReceiptResponse && uploadWarrantyResponse)
                {

//                    product.receiptImage=preSignedReceipt.imageUrl
//                    product.warrantyCardImage=preSignedWarranty.imageUrl

                    val request = product.copy(
                        receiptImage =receiptImageUrl,
                        warrantyCardImage = warrantyImageUrl
                    )


                    when (val result = saveProduct(request)) {

                        is ApiState.Success -> {
                            productDao.updateSyncStatus(product.id,1)
                        }

                        is ApiState.Error -> {
                            throw Exception(result.message)
                        }

                        is ApiState.Loading -> {
                        }

                        else -> {}
                    }

                }

//
//                productRepository.updateSyncStatus(
//                    id = product.id,
//                    markSynced = 1
//                )



            } catch (e: Exception) {

                println(e.toString())
            }
        }

    }


    suspend fun saveProduct(product: Product): ApiState<ApiResponse<Product>> {

        return try {

            val response = productSerivce.saveProduct(product)

            if (response.isSuccessful && response.body() != null) {
                ApiState.Success(response.body()!!)
            } else {
                ApiState.Error(response.message())
            }

        } catch (e: Exception) {
            ApiState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }


    suspend fun saveFcmToken(token: String): ApiState<ApiResponse<String>> {

        return try {

            val response = productSerivce.saveFcmToken(FcmTokenRequest(token))

            if (response.isSuccessful) {
                ApiState.Success(response.body()!!)
            } else {
                ApiState.Error(response.message())
            }

        } catch (e: Exception) {
            ApiState.Error(e.message ?: "Unknown Error")
        }
    }
}