package com.example.warrantyvault.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warrantyvault.features.AddProductScreen
import com.example.warrantyvault.features.HomeScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(navController=navController,
        startDestination = Routes.Home.route)
    {
        composable (Routes.Home.route){
            HomeScreen()
        }

        composable(Routes.AddProduct.route) {
            AddProductScreen()
        }

    }


}