package com.example.unibus.presentation.user

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
class UserHomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageFirebaseRepository: StorageFirebaseRepository,
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user


    private val _driverForBooking = MutableStateFlow<User?>(null)  // حالة السائق المحجوز
    val driverForBooking: StateFlow<User?> = _driverForBooking

    private val _bookedBus = MutableStateFlow<User?>(null)
    val bookedBus: StateFlow<User?> = _bookedBus

    init {
        loadCurrentUser()
        loadBookedBusForUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _user.value = accountRepository.getCurrentUser()
        }
    }

    private fun loadBookedBusForUser() {
        viewModelScope.launch {
            val userId = accountRepository.currentUserId
            userId?.let {
                _bookedBus.value = storageFirebaseRepository.getBookedBusForUser(it)
            }
        }
    }
}