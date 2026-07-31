package com.example.warrantyvault.features

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WarrantyViewModel @Inject constructor() : ViewModel() {

    private val _brand =MutableStateFlow("")
    val brand= _brand.asStateFlow()
    private val _productName = MutableStateFlow("")
    val productName = _productName.asStateFlow()

    private val _purchaseDate = MutableStateFlow(getCurrentDate())
    val purchaseDate=_purchaseDate.asStateFlow()

    val showPurchaseDatePicker = MutableStateFlow(false)


    private val _warrantyDate=MutableStateFlow(getCurrentDate())
    val warrantyDate=_warrantyDate.asStateFlow()
    val showWarrantyDatePicker=MutableStateFlow(false)

    private val _notes= MutableStateFlow("")
    val notes=_notes.asStateFlow()


    private val _showReceiptSheet = MutableStateFlow(false)
    val showReceiptSheet=_showReceiptSheet.asStateFlow()


    private val _showWarrantySheet = MutableStateFlow(false)
    val showWarrantySheet=_showWarrantySheet.asStateFlow()


    private val _receiptImage = MutableStateFlow<Uri?>(null)
    val receiptImage = _receiptImage.asStateFlow()


    private val _warrantyImage = MutableStateFlow<Uri?>(null)
    val warrantyImage = _warrantyImage.asStateFlow()

    fun updateProductName(value: String) {
        _productName.value = value
    }

    fun updateBrand(value: String) {
        _brand.value = value
    }

    fun openPurchaseDatePicker() {
        showPurchaseDatePicker.value = true
    }

    fun closePurchaseDatePicker() {
        showPurchaseDatePicker.value = false
    }

    fun updatePurchaseDate(dateMillis: Long) {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        _purchaseDate.value = formatter.format(Date(dateMillis))
        closePurchaseDatePicker()
    }

    fun openWarrantyDatePicker() {
        showWarrantyDatePicker.value = true

    }

    fun closeWarrantyDatePicker() {
        showWarrantyDatePicker.value = false

    }

    fun updateWarrantyDate(dateMillis:Long)
    {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        _warrantyDate.value = formatter.format(Date(dateMillis))
        closeWarrantyDatePicker()
    }

    fun updateNotes(note:String)
    {
        _notes.value=note
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())
    }

    fun createImageFile(context: Context,title:String): Uri {

        var name=""
        if(title == "receipt")
        {
            name="receipt_"
        }else
        {
            name="warranty_"
        }
        val imageFile = File(
            context.cacheDir,
            "${name +System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }


    fun openReceiptSheet()
    {
        _showReceiptSheet.value=true
    }

    fun closeReceiptSheet()
    {
        _showReceiptSheet.value=false
    }


    fun openWarrantySheet()
    {
        _showWarrantySheet.value=true
    }

    fun closeWarrantySheet()
    {
        _showWarrantySheet.value=false
    }

    fun updateReceiptImage(uri: Uri)
    {
        _receiptImage.value=uri
    }

    fun updateWarrantyImage(uri:Uri)
    {
        _warrantyImage.value=uri
    }



    fun saveProductInfo()
    {
        Log.d("receipt gallery uri", _receiptImage.value.toString())
        Log.d("warranty gallery uri", _warrantyImage.value.toString())
    }


}
