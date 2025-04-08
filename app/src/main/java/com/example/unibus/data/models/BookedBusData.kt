package com.example.unibus.data.models

data class BookedBusData(
    val driverBusId: String = "",
    val driverBusTrip: String = "",
    val driverBusName: String = "",
    val busPrice: Double = 0.0,
    val bookedDate: String = "",
    val bookedTime: String = "",
    val bookedUserId: String = "",
    val phoneNumber: String = "",
    val nameUser: String = "",
    val emailUser: String = "",
    val idNumber: String = "",
    val addressMaps: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val userPhoto: String = "",
)
