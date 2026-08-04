package com.example.warrantyvault.common.model

data class ApiResponse<T>(
    val message: String,
    val status: String,
    val status_code: Int,
    val data: T?,
    val errors: Any?
)