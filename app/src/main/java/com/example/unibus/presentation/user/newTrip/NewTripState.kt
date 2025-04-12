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
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)