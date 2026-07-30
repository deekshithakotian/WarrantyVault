package com.example.warrantyvault.navigation

sealed class Routes(val route: String) {

    object Home :Routes(route="home")
    object AddProduct :Routes(route="add_Product")
}