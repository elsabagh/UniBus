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
    val priceTrip: String = "",
    val available: String = "",
    val betweenAddress: String = "",
    val nationality: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val reservedSeats: String = "",
    val availableSeats: String = "",
    val tripNo: String = "",
    val driverBusId: String = "",
    val driverBusName: String = "",
    val busPrice: String = "",
    val bookedDate: String = "",
    val bookedTime: String = "",
    val bookedUserId: String = "",
    val statusBook: String = "",
    val dateTrip: String = "",
    val timeTrip: String = "",
    val uniAddress: String = ""
)

data class UserWithDocId(
    val user: User,
    val documentId: String
)