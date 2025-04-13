package com.example.unibus.utils


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationFetched: (Double, Double) -> Unit
) {
    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                onLocationFetched(location.latitude, location.longitude)
                fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting location
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}


fun checkIfGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

@SuppressLint("MissingPermission")
fun updateLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationUpdated: (String) -> Unit
) {
    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000 // جلب الموقع كل 5 ثواني
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                val locationString = "${location.latitude},${location.longitude}"
                onLocationUpdated(locationString)
            } ?: run {
                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
            }

            // تأكد من عدم إلغاء التحديثات قبل الحصول على الموقع
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}

fun isLocationPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}