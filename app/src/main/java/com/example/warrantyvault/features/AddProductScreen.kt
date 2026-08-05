package com.example.warrantyvault.features

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.warrantyvault.common.model.ApiState
import kotlinx.coroutines.flow.update


private val BackgroundColor = Color(0xFFF7F7FF)
private val Purple = Color(0xFF5E35B1)
private val LightPurple = Color(0xFFF0EBFF)

@Composable
fun AddProductScreen(
    viewModel: WarrantyViewModel=hiltViewModel(),
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onUploadReceipt: () -> Unit = {},
    onUploadWarranty: () -> Unit = {}
) {

    val context = LocalContext.current


    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var showReceiptPreview by remember { mutableStateOf(false)}
    var showWarrantyPreview by remember { mutableStateOf(false)}
    val showReceiptSheet by viewModel.showReceiptSheet.collectAsState()
    val showWarrantySheet by viewModel.showWarrantySheet.collectAsState()
    val showPurchasePicker by viewModel.showPurchaseDatePicker.collectAsState()
    val showWarrantyPicker by viewModel.showWarrantyDatePicker.collectAsState()

    val product by viewModel.product.collectAsState()

    val brand =product.brand
    val productName =product.productName
    val purchaseDate =product.purchaseDate
    val warrantyDate = product.warrantyDate
    val notes =product.notes


    val saveProduct by viewModel.saveProduct.collectAsState()
    val selectedReceiptUri by viewModel.selectedReceiptUri.collectAsState()
    val selectedWarrantyUri by viewModel.selectedWarrantyUri.collectAsState()

    val galleryLauncherReceipt =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            uri?.let {
                viewModel.updateReceiptUri(it)

                val localPath=viewModel.copyImageToInternalStorage(context,it)
                viewModel.updateReceiptImage(localPath)
            }
        }

    val cameraLauncherReceipt =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                photoUri?.let {
                    viewModel.updateReceiptUri(it)

                    val localPath=viewModel.copyImageToInternalStorage(context,it)
                    viewModel.updateReceiptImage(localPath)
                }
            }
        }



    val galleryLauncherWarranty =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            uri?.let {
                viewModel.updateWarrantyUri(it)


                val localPath=viewModel.copyImageToInternalStorage(context,it)
                viewModel.updateWarrantyImage(localPath)
            }
        }

    val cameraLauncherWarranty =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if(success){
                photoUri?.let {
                    viewModel.updateWarrantyUri(it)

                    val localPath=viewModel.copyImageToInternalStorage(context,it)
                    viewModel.updateWarrantyImage(localPath)
                }
            }
        }

//
//    LaunchedEffect(Unit) {
//
//        viewModel.goBackHome.collect {
//            onBackClick()
//        }
//    }


    LaunchedEffect(saveProduct) {
        when(saveProduct)
        {
            is ApiState.Loading->{


            }

            is ApiState.Success-> {

                Toast.makeText(
                    context,
                    (saveProduct as ApiState.Success).data.message,
                    Toast.LENGTH_SHORT
                ).show()
                onBackClick()
                viewModel.getAllProducts()

            }
            is ApiState.Error->{

                Toast.makeText(
                    context,
                    (saveProduct as ApiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

            }
            else->{

            }


        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        // ---------------- HEADER ----------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 20.dp,
                    top = 32.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF252538),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = "Add Product",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF19192D)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Enter product details to track warranty",
                    fontSize = 14.sp,
                    color = Color(0xFF77778A)
                )
            }
        }

        // ---------------- CONTENT ----------------

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            // Product Name
            ProductInputCard(
                icon = Icons.Default.Inventory2,
                title = "Product Name",
                hint = "Enter product name",
                value = productName,
                onValueChange = {
                    viewModel.updateProductName(it)
                }
            )

            // Brand
            ProductInputCard(
                icon = Icons.Default.Tag,
                title = "Brand",
                hint = "Enter brand name",
                value = brand,
                onValueChange = {
                   viewModel.updateBrand(it)
                }
            )

            // Purchase Date
            DateCard(
                icon = Icons.Default.CalendarMonth,
                title = "Purchase Date",
                date = purchaseDate,
                onClick = viewModel::openPurchaseDatePicker
            )

            ShowDatePicker(
                showPicker = showPurchasePicker,
                onDismiss = viewModel::closePurchaseDatePicker,
                onDateSelected = viewModel::updatePurchaseDate
            )


            // Warranty End Date
            DateCard(
                icon = Icons.Default.Security,
                title = "Warranty End Date",
                date = warrantyDate,
                onClick = viewModel::openWarrantyDatePicker

            )

            ShowDatePicker(
                showPicker = showWarrantyPicker,
                onDismiss = viewModel::closeWarrantyDatePicker,
                onDateSelected = viewModel::updateWarrantyDate
            )

            // Notes
            NotesCard(
                value=notes,
                hint = "Enter notes",
                onValueChange = viewModel::updateNotes
            )

            // Purchase Receipt
            UploadCard(
                title = "Purchase Receipt",
                subtitle = "Upload receipt for this product",
                buttonText = "Upload Receipt",
                imageUri=selectedReceiptUri,
                icon = Icons.Default.ReceiptLong,
                buttonColor = Color(0xFFFF8A00),
                cardBackground = Color(0xFFFFF8F0),
                onClick = viewModel::openReceiptSheet,
                onImageClick = {
                   showReceiptPreview=true
                }
            )

            ImagePickerBottomSheet(
                showSheet = showReceiptSheet,
                onDismiss = viewModel::closeReceiptSheet,
                onCameraClick = {
                    photoUri = viewModel.createImageFile(context,"receipt")
                    cameraLauncherReceipt.launch(photoUri!!)
                },
                onGalleryClick = {
                    galleryLauncherReceipt.launch("image/*")
                }
            )

            ImagePreviewDialog(
                image = selectedReceiptUri,
                showDialog = showReceiptPreview,
                onDismiss = {
                    showReceiptPreview = false
                }
            )

            // Warranty Document
            UploadCard(
                title = "Warranty Document",
                subtitle = "Upload warranty document",
                imageUri=selectedWarrantyUri,
                buttonText = "Upload Document",
                icon = Icons.Default.Description,
                buttonColor = Color(0xFF1976D2),
                cardBackground = Color(0xFFF0F6FF),
                onClick = viewModel::openWarrantySheet,
                onImageClick = {
                    showWarrantyPreview=true
                }
            )


            ImagePickerBottomSheet(
                showSheet = showWarrantySheet,
                onDismiss = viewModel::closeWarrantySheet,
                onCameraClick = {
                    photoUri = viewModel.createImageFile(context,"warranty")
                    cameraLauncherWarranty.launch(photoUri!!)

                },
                onGalleryClick = {
                    galleryLauncherWarranty.launch("image/*")

                }
            )

            ImagePreviewDialog(
                image = selectedWarrantyUri,
                showDialog = showWarrantyPreview,
                onDismiss = {
                    showWarrantyPreview = false
                }
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        // ---------------- SAVE BUTTON ----------------

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 10.dp,
                    bottom = 18.dp
                )
                .height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple
            )
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Save",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ProductInputCard(
    icon: ImageVector,
    title: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PurpleIconBox(
                icon = icon
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF282842)
                )

                Spacer(modifier = Modifier.height(4.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF202034)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->

                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                fontSize = 15.sp,
                                color = Color(0xFF858599)
                            )
                        }

                        innerTextField()
                    }
                )
            }
        }
    }
}

@Composable
fun DateCard(
    icon: ImageVector,
    title: String,
    date: String,
    onClick:()->Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PurpleIconBox(
                icon = icon
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF282842)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = date,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF202034)
                )
            }

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color(0xFF29294A)
            )
        }
    }
}


@Composable
fun ShowDatePicker(
    showPicker: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    if (!showPicker) return

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


@Composable
fun NotesCard(value:String="",hint:String="Enter notes",onValueChange:(String)->Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            PurpleIconBox(
                icon = Icons.Default.Note
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = "Notes (Optional)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF282842)
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF202034)
                    ),
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->

                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                fontSize = 15.sp,
                                color = Color(0xFF858599)
                            )
                        }

                        innerTextField()
                    }
                )
            }
        }
    }
}

@Composable
fun PurpleIconBox(
    icon: ImageVector
) {

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(LightPurple),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(27.dp),
            tint = Purple
        )
    }
}


@Composable
fun UploadCard(
    title: String,
    subtitle: String,
    buttonText: String,
    imageUri: Uri?,
    icon: ImageVector,
    buttonColor: Color,
    cardBackground: Color,
    onClick: () -> Unit,
    onImageClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = buttonColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = buttonColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (imageUri == null)
                            subtitle
                        else
                            "$title uploaded successfully",
                        fontSize = 13.sp,
                        color = Color(0xFF737386)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (imageUri == null) {

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text =buttonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

            } else {

                AsyncImage(
                    model = imageUri,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onImageClick()
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        buttonColor
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = buttonColor
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Change $title",
                        color = buttonColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (!showSheet) return

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Text(
            text = "Upload Purchase Receipt",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(20.dp)
        )

        HorizontalDivider()

        ListItem(
            headlineContent = { Text("Take Photo") },
            leadingContent = {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null
                )
            },
            modifier = Modifier.clickable {

                onCameraClick()
                onDismiss()
            }
        )

        ListItem(
            headlineContent = { Text("Choose from Gallery") },
            leadingContent = {
                Icon(
                    Icons.Default.PhotoLibrary,
                    contentDescription = null
                )
            },
            modifier = Modifier.clickable {
                onGalleryClick()
                onDismiss()
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun ImagePreviewDialog(
    image: Uri?,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {

    if (!showDialog || image == null) return

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column {

                AsyncImage(
                    model = image,
                    contentDescription = "Preview Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    contentScale = ContentScale.Fit
                )

                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onDismiss
                ) {
                    Text("Close")
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddProductPreview()
{
    val viewModel: WarrantyViewModel = hiltViewModel()

    AddProductScreen(viewModel,
        onSaveClick={},
        onBackClick={},
        onUploadReceipt={},
        onUploadWarranty={})
}