package com.example.warrantyvault.features

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val BackgroundColor = Color(0xFFF7F7FF)
private val Purple = Color(0xFF5E35B1)
private val LightPurple = Color(0xFFF0EBFF)

@Composable
fun AddProductScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onUploadReceipt: () -> Unit = {},
    onUploadWarranty: () -> Unit = {}
) {

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
                hint = "Enter product name"
            )

            // Brand
            ProductInputCard(
                icon = Icons.Default.Tag,
                title = "Brand",
                hint = "Enter brand name"
            )

            // Purchase Date
            DateCard(
                icon = Icons.Default.CalendarMonth,
                title = "Purchase Date",
                date = "27 May 2025"
            )

            // Warranty End Date
            DateCard(
                icon = Icons.Default.Security,
                title = "Warranty End Date",
                date = "27 May 2028"
            )

            // Notes
            NotesCard()

            // Purchase Receipt
            UploadCard(
                title = "Purchase Receipt",
                subtitle = "Upload receipt for this product",
                buttonText = "Upload Receipt",
                icon = Icons.Default.ReceiptLong,
                buttonColor = Color(0xFFFF8A00),
                cardBackground = Color(0xFFFFF8F0),
                onClick = onUploadReceipt
            )

            // Warranty Document
            UploadCard(
                title = "Warranty Document",
                subtitle = "Upload warranty document",
                buttonText = "Upload Document",
                icon = Icons.Default.Description,
                buttonColor = Color(0xFF1976D2),
                cardBackground = Color(0xFFF0F6FF),
                onClick = onUploadWarranty
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
    hint: String
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

            Column {

                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF282842)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = hint,
                    fontSize = 15.sp,
                    color = Color(0xFF858599)
                )
            }
        }
    }
}


@Composable
fun DateCard(
    icon: ImageVector,
    title: String,
    date: String
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
fun NotesCard() {

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

                Text(
                    text = "Add any notes about the product",
                    fontSize = 15.sp,
                    color = Color(0xFF858599)
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
    icon: ImageVector,
    buttonColor: Color,
    cardBackground: Color,
    onClick: () -> Unit
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
                        .background(
                            Color.White.copy(alpha = 0.7f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(27.dp),
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

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color(0xFF737386)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

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
                    modifier = Modifier.size(22.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddProductPreview()
{
    AddProductScreen()
}