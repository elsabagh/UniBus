package com.example.unibus.presentation.driver.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.Notification
import com.example.unibus.data.models.User
import com.example.unibus.data.models.UserWithDocId
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class NotificationDriverViewModel @Inject constructor(
    private val storageRepository: StorageFirebaseRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<UserWithDocId>>(emptyList())
    val notifications: StateFlow<List<UserWithDocId>> = _notifications

    init {
        viewModelScope.launch {
            val currentUser = accountRepository.getCurrentUser()
            val tripNo = currentUser?.tripNo ?: ""
            if (tripNo.isNotBlank()) {
                loadNotificationsForTrip(tripNo, "processing")
            }
        }
    }


    fun loadNotificationsForTrip(tripNo: String, status: String) {
        viewModelScope.launch {
            val bookings = storageRepository.getBookingsForTripNot(tripNo, status)
            _notifications.value = bookings
        }
    }

    fun approveBooking(documentId: String, driverBusId: String) {
        viewModelScope.launch {
            try {
                storageRepository.approveBooking(documentId)
                storageRepository.updateDriverSeatCount(driverBusId)
                refreshBookings()
            } catch (e: Exception) {
                Log.e("NotificationVM", "Error approving booking: ${e.message}")
            }
        }
    }

    fun rejectBooking(documentId: String) {
        viewModelScope.launch {
            storageRepository.rejectBooking(documentId)
            refreshBookings()
        }
    }

    private fun refreshBookings() {
        viewModelScope.launch {
            val currentUser = accountRepository.getCurrentUser()
            currentUser?.tripNo?.let {
                loadNotificationsForTrip(it, "processing")
            }
        }
    }

    fun sendCustomNotificationToUser(user: User, title: String, message: String, type: String) {
        viewModelScope.launch {
            try {
                val currentDriver = accountRepository.getCurrentUser()

                val dateNow = SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
                val timeNow = SimpleDateFormat("hh:mm a").format(java.util.Date())

                val notification = Notification(
                    title = title,
                    message = message,
                    date = dateNow,
                    time = timeNow,
                    userId = user.userId,
                    userName = user.userName,
                    nameDriver = currentDriver?.userName ?: "Unknown Driver",
                    notificationType = type,
                    addressMaps = user.addressMaps
                )
                storageRepository.createUserNotification(notification)
                Log.d("NotificationVM", "Custom notification sent.")
            } catch (e: Exception) {
                Log.e("NotificationVM", "Failed to send custom notification: ${e.message}")
            }
        }
    }


}