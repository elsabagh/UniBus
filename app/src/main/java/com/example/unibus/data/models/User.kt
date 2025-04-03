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
    val betweenAddress: String = "",
    val latitude: String = "",
    val longitude: String = "",
)
