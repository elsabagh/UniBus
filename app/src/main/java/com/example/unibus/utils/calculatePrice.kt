package com.example.unibus.utils


fun calculatePrice(distanceInMeters: Double): Double {
    val priceInKuwaitiDinars = (distanceInMeters / 100.0) * 0.1

    val finalPrice = when {
        priceInKuwaitiDinars < 1 -> 1.0
        priceInKuwaitiDinars > 5 -> 5.0
        else -> priceInKuwaitiDinars
    }

    return finalPrice
}

fun formatToTwoDecimalPlaces(value: Double): String {
    return String.format("%.2f", value)  // التقريب إلى خانتين عشريتين
}