package com.example.warrantyvault.common.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("select * from products where markSynced='0'")
    suspend fun getUnSyncedProducts(): List<Product>

    @Query("select * from products where markSynced='1'")
    suspend fun getSyncedProducts(): List<Product>

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>


    @Query("UPDATE products SET markSynced=:markSynced where id=:id")
    suspend fun updateSyncStatus(id:Int,markSynced:Int)
}