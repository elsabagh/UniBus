package com.example.unibus.presentation.user.availableBuses

data class AvailableBusesState(
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val nationality: String = "",
    val role: String = "",
    val reservedSeats: String = "",
    val availableSeats: String = "",
    val tripNo: String = "",
    val selectLocation: String = "",
    val newSelectTime: String = "",
    val date: String = "",
    val availableDrivers: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)