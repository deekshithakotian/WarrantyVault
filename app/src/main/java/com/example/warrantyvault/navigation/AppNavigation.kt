package com.example.warrantyvault.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warrantyvault.features.AddProductScreen
import com.example.warrantyvault.features.HomeScreen
import com.example.warrantyvault.features.WarrantyViewModel


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(navController=navController,
        startDestination = Routes.Home.route)
    {
        composable (Routes.Home.route){
            HomeScreen(onAddProductClick = {
                navController.navigate(Routes.AddProduct.route)
            })
        }

        composable(Routes.AddProduct.route) {
            val viewModel: WarrantyViewModel = hiltViewModel()

            AddProductScreen(viewModel,
                onSaveClick={
                    viewModel.saveProductInfo()
                },
                onBackClick={
                    navController.popBackStack()
                },
               onUploadReceipt={},
                onUploadWarranty={},
               )
        }

    }


}