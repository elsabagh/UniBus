package com.example.unibus.presentation.driver

import android.util.Log
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
class DriverViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {
    private val _driver = MutableStateFlow<User?>(null)
    val driver: StateFlow<User?> = _driver

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _driver.value = accountRepository.getCurrentUser()
        }
    }

    fun emptyBus() {
        viewModelScope.launch {
            val currentDriver = _driver.value ?: return@launch
            val tripNo = currentDriver.tripNo
            val driverId = currentDriver.userId

            try {
                storageRepository.clearTripBookings(tripNo)
                storageRepository.resetDriverSeats(driverId)

                loadCurrentUser()
            } catch (e: Exception) {
                Log.e("DriverViewModel", "Failed to empty bus: ${e.message}")
            }
        }
    }

}