package com.example.warrantyvault

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WarrantyVaultApplication() : Application(), Configuration.Provider {


    override fun onCreate() {
        super.onCreate()

        android.util.Log.d("WORKER_TEST", workerFactory.javaClass.name)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}