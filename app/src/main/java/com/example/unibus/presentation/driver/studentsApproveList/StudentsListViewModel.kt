package com.example.unibus.presentation.driver.studentsApproveList

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
class StudentsListViewModel@Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
): ViewModel() {

    private val _studentsState = MutableStateFlow<List<User>>(emptyList())
    val studentsState: StateFlow<List<User>> = _studentsState

    init {
        viewModelScope.launch {
            val currentUser = accountRepository.getCurrentUser()
            val tripNo = currentUser?.tripNo ?: ""
            if (tripNo.isNotBlank()) {
                loadNotificationsForTrip(tripNo, "approved")
            }
        }
    }

    fun loadNotificationsForTrip(tripNo: String, status: String) {
        viewModelScope.launch {
            val bookings = storageRepository.getBookingsForTrip(tripNo, status)
            _studentsState.value = bookings
        }
    }
}