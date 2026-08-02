package com.example.warrantyvault.features

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class WarrantyViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

//    private val _brand =MutableStateFlow("")
//    val brand= _brand.asStateFlow()

//    private val _productName = MutableStateFlow("")
//    val productName = _productName.asStateFlow()
//
//    private val _purchaseDate = MutableStateFlow(getCurrentDate())
//    val purchaseDate=_purchaseDate.asStateFlow()

    //    private val _warrantyDate=MutableStateFlow(getCurrentDate())
//    val warrantyDate=_warrantyDate.asStateFlow()

//    private val _notes= MutableStateFlow("")
//    val notes=_notes.asStateFlow()

    private val _receiptImage = MutableStateFlow<Uri?>(null)
    val receiptImage = _receiptImage.asStateFlow()


    private val _warrantyImage = MutableStateFlow<Uri?>(null)
    val warrantyImage = _warrantyImage.asStateFlow()

    val showPurchaseDatePicker = MutableStateFlow(false)
    val showWarrantyDatePicker=MutableStateFlow(false)

    private val _showReceiptSheet = MutableStateFlow(false)
    val showReceiptSheet=_showReceiptSheet.asStateFlow()


    private val _showWarrantySheet = MutableStateFlow(false)
    val showWarrantySheet=_showWarrantySheet.asStateFlow()





    private val _product = MutableStateFlow(Product())
    val product = _product.asStateFlow()

    private val _goBackHome= MutableSharedFlow<String>()
    val goBackHome=_goBackHome.asSharedFlow()

    fun updateProductName(value: String) {
        _product.update {
            it.copy(productName=value)
        }
    }

    fun updateBrand(value: String) {
        _product.update {
            it.copy(brand =value)
        }
    }

    fun updatePurchaseDate(dateMillis: Long) {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        _product.update {
            it.copy(purchaseDate =formatter.format(Date(dateMillis)))
        }
        closePurchaseDatePicker()
    }

    fun updateWarrantyDate(dateMillis:Long)
    {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        _product.update {
            it.copy(warrantyDate =formatter.format(Date(dateMillis)))
        }
        closeWarrantyDatePicker()
    }

    fun updateNotes(note:String)
    {
        _product.update {
            it.copy(notes=note)
        }
    }

    fun updateReceiptImage(uri: Uri)
    {
        _product.update {
            it.copy(receiptImage = uri.toString())
        }
    }

    fun updateWarrantyImage(uri:Uri)
    {
        _product.update {
            it.copy(warrantyCardImage = uri.toString())
        }
    }



    fun openPurchaseDatePicker() {
        showPurchaseDatePicker.value = true
    }

    fun closePurchaseDatePicker() {
        showPurchaseDatePicker.value = false
    }



    fun openWarrantyDatePicker() {
        showWarrantyDatePicker.value = true

    }

    fun closeWarrantyDatePicker() {
        showWarrantyDatePicker.value = false

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




    fun saveProductInfo()
    {
       viewModelScope.launch {

           withContext(Dispatchers.IO)
           {
               _product.update {
                   it.copy(markSynced = 0)
               }
               productRepository.insertProduct(_product.value)

           }
           _goBackHome.emit("go_back_home")

       }
    }


}
