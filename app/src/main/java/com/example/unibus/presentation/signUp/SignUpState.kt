package com.example.unibus.presentation.signUp

data class SignUpState(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val idNumber: String = "",
    var addressMaps: String = "",
    val dateTrip: String = "",
    val timeTrip: String = "",
    var userPhoto: String = "",
    val role: String = "user",
    val betweenAddress: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
