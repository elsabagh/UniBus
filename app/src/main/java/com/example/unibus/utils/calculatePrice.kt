package com.example.unibus.utils


fun calculatePrice(distanceInMeters: Double): Double {
    val priceInDurhams = (distanceInMeters / 1000.0) * 0.1

    val finalPrice = when {
        priceInDurhams < 1 -> 1.0
        priceInDurhams > 5 -> 5.0
        else -> priceInDurhams
    }

    return finalPrice
}

fun formatToTwoDecimalPlaces(value: Double): String {
    return String.format("%.2f", value)
}