package com.example.unibus.presentation.user.newTrip

data class NewTripState(
    val userId: String = "",
    val newSelectTime: String = "",
    val date: String = "",
    val selectLocation: String = "",
    val tripType: String = "",
    val tripName: String = "",
    val tripDescription: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val isLoading: Boolean = false,  // حالة التحميل أثناء التسجيل
    val isSuccess: Boolean = false,  // حالة النجاح بعد التسجيل
    val errorMessage: String? = null // تخزين أي رسالة خطأ
)