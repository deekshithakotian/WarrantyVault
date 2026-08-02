package com.example.warrantyvault.common.local.helperclass

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.warrantyvault.repository.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val productRepository: ProductRepository
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val products = productRepository.getUnsyncedProducts()

        products.forEach { product ->

            try {

                // Upload receipt image

                // Upload warranty image

                // Upload product image

                // Call backend API
//
//                productRepository.updateSyncStatus(
//                    id = product.id,
//                    markSynced = 1
//                )



            } catch (e: Exception) {

                return Result.retry()
            }
        }

        return Result.success()
    }

}