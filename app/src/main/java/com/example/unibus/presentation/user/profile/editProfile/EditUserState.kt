package com.example.unibus.presentation.user.profile.editProfile

data class EditUserState(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val idNumber: String = "",
    var addressMaps: String = "",
    var userPhoto: String = "",
    val betweenAddress: String = "",
    val role: String = "user",
    val password: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val statusAccount: String = "accepted",
    val latitude: String = "",
    val longitude: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val error: String? = null,
    val isPasswordChanged: Boolean = false

)