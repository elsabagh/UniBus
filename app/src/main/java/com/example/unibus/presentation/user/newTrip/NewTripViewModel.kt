package com.example.unibus.presentation.user.newTrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTripViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageFirebaseRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewTripState())
    val state: StateFlow<NewTripState> = _state.asStateFlow()


    fun onSelectTimeChange(newSelectTime: String) {
        _state.value = _state.value.copy(newSelectTime = newSelectTime)
    }

    fun onDateChange(newDate: String) {
        _state.value = _state.value.copy(date = newDate)
    }

    fun onLocationChange(newLocation: String) {
        _state.value = _state.value.copy(selectLocation = newLocation)
    }

    fun updateUserTrip() {
        val userId = accountRepository.currentUserId
        val userData = state.value

        val updatedUser = User(
            userId = userId,
            addressMaps = userData.selectLocation,
            dateTrip = userData.date,
            timeTrip = userData.newSelectTime
        )

        viewModelScope.launch {
            try {
                storageFirebaseRepository.updateUserTrip(updatedUser)
                _state.value = _state.value.copy(isSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Failed to update data: ${e.message}"
                )
            }
        }
    }

}