package com.example.warrantyvault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warrantyvault.features.AddProductScreen
import com.example.warrantyvault.features.HomeScreen
import com.example.warrantyvault.features.LoginScreen
import com.example.warrantyvault.features.LoginViewModel
import com.example.warrantyvault.features.WarrantyViewModel


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: WarrantyViewModel = hiltViewModel()

    NavHost(navController=navController,
        startDestination = Routes.Login.route)
    {

        composable(Routes.Login.route)
        {
            val viewModel: LoginViewModel=hiltViewModel()
            LoginScreen(viewModel,onSuccessLogin={
                navController.navigate(Routes.Home.route)
            })
        }

        composable (Routes.Home.route){
//            val viewModel: WarrantyViewModel = hiltViewModel()

            HomeScreen(viewModel=viewModel,
            onAddProductClick = {
                navController.navigate(Routes.AddProduct.route)
            })
        }

        composable(Routes.AddProduct.route) {
//            val viewModel: WarrantyViewModel = hiltViewModel()

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

