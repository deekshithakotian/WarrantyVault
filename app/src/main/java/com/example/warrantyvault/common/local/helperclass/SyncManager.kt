package com.example.warrantyvault.common.local.helperclass

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SyncManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun startProductSync() {

        val request =
            OneTimeWorkRequestBuilder<ProductSyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "product_sync",
                ExistingWorkPolicy.KEEP,
                request
            )
    }

}