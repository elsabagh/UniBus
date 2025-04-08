package com.example.unibus.presentation.driver.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationDriverViewModel @Inject constructor(
    private val storageRepository: StorageFirebaseRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<User>>(emptyList())
    val notifications: StateFlow<List<User>> = _notifications

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
            val bookings = storageRepository.getBookingsForTrip(tripNo, status)
            _notifications.value = bookings
        }
    }

    fun approveBooking(userId: String) {
        viewModelScope.launch {
            storageRepository.approveBooking(userId)
            refreshBookings()
        }
    }

    fun rejectBooking(userId: String) {
        viewModelScope.launch {
            storageRepository.rejectBooking(userId)
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
}