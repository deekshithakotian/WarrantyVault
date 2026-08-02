package com.example.warrantyvault.repository

import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.common.local.ProductDao
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getUnsyncedProducts(): List<Product> {
        return productDao.getUnSyncedProducts()

    }


    suspend fun updateSyncStatus(id: Int, markSynced: Int) {
        productDao.updateSyncStatus(id, markSynced)

    }
}