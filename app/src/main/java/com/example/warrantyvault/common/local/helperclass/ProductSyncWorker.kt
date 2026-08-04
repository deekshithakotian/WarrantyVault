package com.example.warrantyvault.common.local.helperclass

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.warrantyvault.repository.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val productRepository: ProductRepository
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

       productRepository.syncToCloud(applicationContext)
        return Result.success()
    }

}