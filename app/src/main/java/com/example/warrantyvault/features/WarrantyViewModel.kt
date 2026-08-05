package com.example.warrantyvault.features

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyvault.common.local.Product
import com.example.warrantyvault.common.local.helperclass.SyncManager
import com.example.warrantyvault.common.model.ApiResponse
import com.example.warrantyvault.common.model.ApiState
import com.example.warrantyvault.common.model.DashboardUiState
import com.example.warrantyvault.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@HiltViewModel
class WarrantyViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private  val syncManager: SyncManager
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

    private val _productSaveState = MutableStateFlow<ApiState<ApiResponse<Product>>>(ApiState.Loading)
    val saveProduct=_productSaveState.asStateFlow()



    private val _selectedReceiptUri = MutableStateFlow<Uri?>(null)
    val selectedReceiptUri = _selectedReceiptUri.asStateFlow()

    private val _selectedWarrantyUri = MutableStateFlow<Uri?>(null)
    val selectedWarrantyUri = _selectedWarrantyUri.asStateFlow()


    private val _productsList=MutableStateFlow<List<Product>>(emptyList())
    val productsList=_productsList.asStateFlow()

    private val _listProductResponse=MutableSharedFlow<String>()
    val listProductResponse= _listProductResponse.asSharedFlow()

    private val _dashboardState = MutableStateFlow(DashboardUiState())
    val dashboardState = _dashboardState.asStateFlow()


    init {
        getAllProducts()
    }

    fun updateReceiptUri(uri: Uri) {
        _selectedReceiptUri.value = uri
    }

    fun updateWarrantyUri(uri: Uri) {
        _selectedWarrantyUri.value = uri
    }

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

    fun updateReceiptImage(uri: String)
    {
        _product.update {
            it.copy(receiptImage = uri)
        }
    }

    fun updateWarrantyImage(uri:String)
    {
        _product.update {
            it.copy(warrantyCardImage = uri)
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

    fun copyImageToInternalStorage(
        context: Context,
        uri: Uri
    ): String {

        val extension = context.contentResolver.getType(uri)
            ?.substringAfter("/")
            ?: "jpg"

        val fileName = "${System.currentTimeMillis()}.$extension"

        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
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


    fun getAllProducts() {

        viewModelScope.launch(Dispatchers.IO) {

                when (val products=productRepository.getAllProducts()) {
                    is ApiState.Success -> {
                        _productsList.value = (products as ApiState.Success).data
                        updateDashBoardState(_productsList.value)

                    }

                    is ApiState.Error -> {
                        _listProductResponse.emit(products.message)
                    }

                    else -> Unit


                }

        }
    }

    fun saveProductInfo()
    {

        _productSaveState.value = ApiState.Loading

        viewModelScope.launch {

            try {

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(_product.value)
                }

                _productSaveState.value = ApiState.Success(ApiResponse("Product Saved","success",200,_product.value,null))

                syncManager.startProductSync()
//            _goBackHome.emit("go_back_home")


            } catch (e: Exception) {

                _productSaveState.value =
                    ApiState.Error(e.message ?: "Unable to save")
            }
        }

    }


    fun updateDashBoardState(product:List<Product>)
    {
        val today= LocalDate.now()

        val expired=product.count {
            val warrantyDate = LocalDate.parse(
                it.warrantyDate,
                DateTimeFormatter.ofPattern("dd MMM yyyy")
            )
            warrantyDate.isBefore(today)
        }


        val expiringSoon=product.count{
            val warrantyDate = LocalDate.parse(
                it.warrantyDate,
                DateTimeFormatter.ofPattern("dd MMM yyyy")
            )
            !warrantyDate.isBefore(today) &&
             ChronoUnit.DAYS.between(today, warrantyDate) <= 30

        }

        val active=product.size-expired

        _dashboardState.update {
            it.copy(
                totalProducts = product.size,
                activeProducts = active,
                expiringSoon = expiringSoon,
                expiredProducts = expired
            )
        }

    }


}
