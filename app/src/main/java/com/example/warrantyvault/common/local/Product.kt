package com.example.warrantyvault.common.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "products")
data class Product(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val productName: String = "",

    val brand: String = "",

    val purchaseDate: String = getCurrentDate(),

    val warrantyDate: String = getCurrentDate(),

    val notes: String = "",

    var receiptImage: String? = null,

    var warrantyCardImage: String? = null,

    val productImage: String? = null,

    val markSynced:Int=0
)

private fun getCurrentDate(): String {
    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(Date())
}
