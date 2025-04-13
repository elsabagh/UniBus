package com.example.unibus.presentation.driver.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.Notification
import com.example.unibus.data.models.User
import com.example.unibus.data.models.UserWithDocId
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import com.example.unibus.utils.DriverNotificationPrefs
import com.example.unibus.utils.NotificationUtilDriver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class NotificationDriverViewModel @Inject constructor(
    private val storageRepository: StorageFirebaseRepository,
    private val accountRepository: AccountRepository,
    private val context: Context
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<UserWithDocId>>(emptyList())
    val notifications: StateFlow<List<UserWithDocId>> = _notifications

    private val _hasNotifications = MutableStateFlow(false)
    val hasNotifications: StateFlow<Boolean> = _hasNotifications


    init {
        viewModelScope.launch {
            getTripNo { tripNo ->
                loadNotificationsForTrip(tripNo, "processing", context)
            }
        }
    }

    fun getTripNo(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val currentUser = accountRepository.getCurrentUser()
            onResult(currentUser?.tripNo ?: "")
        }
    }



    fun loadNotificationsForTrip(tripNo: String, status: String, context: Context) {
        viewModelScope.launch {
            // جلب الحجزات الجديدة
            val bookings = storageRepository.getBookingsForTripNot(tripNo, status)
            _notifications.value = bookings

            // استخراج معرّفات documentId للحجوزات
            val currentDocumentIds = bookings.map { it.documentId }.toSet()
            val notifiedDocumentIds = DriverNotificationPrefs.getNotifiedIds(context)

            // تحديد المعرفات الجديدة (documentId) التي لم يتم إشعارها بعد
            val newDocumentIds = currentDocumentIds.subtract(notifiedDocumentIds)

            // إرسال إشعار لكل معرّف جديد لم يتم إخطار السائق به
            if (newDocumentIds.isNotEmpty()) {
                newDocumentIds.forEach { documentId ->
                    // إرسال إشعار للحجز الذي تم إضافته
                    val userBooking = bookings.first { it.documentId == documentId }
                    NotificationUtilDriver.showDriverNotification(
                        context,
                        "New Request for Trip",
                        "You have a new request from ${userBooking.user.userName}. Tap to review."
                    )

                    // تحديث المعرفات المخزنة مع المعرفات الجديدة
                    val updatedNotifiedDocumentIds = notifiedDocumentIds.union(setOf(documentId))
                    DriverNotificationPrefs.saveNotifiedIds(context, updatedNotifiedDocumentIds)
                }
            }

            // تعيين قيمة hasNotifications بناءً على وجود الحجوزات
            _hasNotifications.value = bookings.isNotEmpty()
        }
    }


    fun markNotificationsAsRead() {
        _hasNotifications.value = false
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
                loadNotificationsForTrip(it, "processing", context)
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