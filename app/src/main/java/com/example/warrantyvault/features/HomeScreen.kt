package com.example.warrantyvault.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onAddProductClick:()->Unit)
{

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        topBar={
            HeaderSection()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                containerColor = Color(0xFF5E35B1),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product"
                )
            }
        }
    ) { paddingValues->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 90.dp
            ),

            verticalArrangement = Arrangement.spacedBy(18.dp)

        ) {

            item {
                TopCard()
            }

            item {

                WarrantySummaryCards()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F8FC))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton( onClick = {}) {
          Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {},
            modifier= Modifier.size(24.dp)) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }



    }
}


@Composable
fun TopCard()
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Good morning, Deekshitha ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Keep track of your warranties\nand never miss an expiry date.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B6B73),
                lineHeight = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFDCE8FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Warranty protection",
                modifier = Modifier.size(38.dp),
                tint = Color(0xFF3976E8)
            )
        }
    }
}

@Composable
fun WarrantySummaryCards() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SummaryCard(
                modifier = Modifier.weight(1f),
                title="Total Products",
                value = "24",
                label = "Products",
                icon = Icons.Default.Inventory,
                color=Color(0xFF7B61FF)
            )

            SummaryCard(
                modifier = Modifier.weight(1f),
                title="Active Warranties",
                value = "18",
                label = "Active",
                icon = Icons.Default.Verified,
                color=Color(0xFFE65100)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SummaryCard(
                modifier = Modifier.weight(1f),
                title="Expiring Soon",
                value = "3",
                label = "Expiring Soon",
                icon = Icons.Default.Warning,
                color=Color(0xFFC62828)
            )

            SummaryCard(
                modifier = Modifier.weight(1f),
                title="Expired",
                value = "6",
                label = "Expired",
                icon = Icons.Default.ErrorOutline,
                color=Color(0xFF6A1B9A)
            )
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    label: String,
    color:Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Column 1 - Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = Color.White

                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Column 2 - Text
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF707070)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202124)
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF8A8A8A)
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview()
{
    HomeScreen(onAddProductClick = {})
}


