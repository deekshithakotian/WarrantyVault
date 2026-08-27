package com.example.warrantyvault.common.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Product::class],
    version=1,
    exportSchema = false)


abstract  class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

}