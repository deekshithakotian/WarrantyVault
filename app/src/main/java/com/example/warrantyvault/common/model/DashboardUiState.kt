package com.example.warrantyvault.common.model

data class DashboardUiState(
    val totalProducts: Int = 0,
    val activeProducts: Int = 0,
    val expiringSoon: Int = 0,
    val expiredProducts: Int = 0
)