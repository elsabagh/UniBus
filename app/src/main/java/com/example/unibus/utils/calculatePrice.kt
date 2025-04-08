package com.example.unibus.utils


fun calculatePrice(distanceInMeters: Int): String {
    val priceInKuwaitiDinars = (distanceInMeters / 100.0) * 0.1

    val finalPrice = when {
        priceInKuwaitiDinars < 1 -> 1.0
        priceInKuwaitiDinars > 5 -> 5.0
        else -> priceInKuwaitiDinars
    }

    return "%.2f".format(finalPrice)
}