package com.example.unibus.data.models

data class User(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val idNumber: String = "",
    var addressMaps: String = "",
    var userPhoto: String = "",
    val role: String = "user",
    val available: String = "",
    val betweenAddress: String = "",
    val nationality: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val reservedSeats: String = "",
    val availableSeats: String = "",
    val tripNo: String = "",
)
