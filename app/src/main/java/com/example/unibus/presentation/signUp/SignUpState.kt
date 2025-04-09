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
    val isLoading: Boolean = false,  // حالة التحميل أثناء التسجيل
    val isSuccess: Boolean = false,  // حالة النجاح بعد التسجيل
    val errorMessage: String? = null // تخزين أي رسالة خطأ
)
