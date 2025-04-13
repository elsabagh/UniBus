package com.example.unibus.presentation.driver.userLocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.Notification
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {


    fun sendArrivalNotification(
        userId: String,
        userName: String,
    ) {
        viewModelScope.launch {
            try {
                val dateNow = SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
                val timeNow = SimpleDateFormat("hh:mm a").format(java.util.Date())

                val notification = Notification(
                    title = "arrived",
                    message = "The bus has arrived at your location.",
                    date = dateNow,
                    time = timeNow,
                    userId = userId,
                    userName = userName,
                    notificationType = "approved",
                    addressMaps = "" // Update if location data is required
                )

                storageRepository.createUserNotification(notification)
                Log.d("UserLocationViewModel", "Notification Sent Successfully")
            } catch (e: Exception) {
                Log.e("UserLocationViewModel", "Failed to Send Notification: ${e.message}")
            }
        }
    }
}