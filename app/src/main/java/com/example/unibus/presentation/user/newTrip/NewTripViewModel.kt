package com.example.unibus.presentation.user.newTrip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.GraphHopperRepository
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
    private val storageFirebaseRepository: StorageFirebaseRepository,
    private val graphHopperRepository: GraphHopperRepository
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
        viewModelScope.launch {
                val fixedAddress = "30.026613, 31.211237"
            val distance = getDistance(fixedAddress, newLocation)
            _state.value = _state.value.copy(betweenAddress = distance.toString())
        }
    }


    suspend fun getDistance(start: String, end: String): Double {
        val apiKey = "d5162feb-126a-4fa4-9aef-6843749d215f"
        var distance = 0.0

        graphHopperRepository.getRoute(start, end, apiKey).collect { result ->
            result.onSuccess { path ->
                distance = path.distance.toDouble()
                Log.d("AvailableBusesViewModel", "Distance: $distance meters")
            }
        }

        return distance
    }

    fun updateUserTrip() {
        val userId = accountRepository.currentUserId
        val userData = state.value

        viewModelScope.launch {
            try {
                val updatedUser = User(
                    userId = userId,
                    addressMaps = userData.selectLocation,
                    dateTrip = userData.date,
                    timeTrip = userData.newSelectTime,
                    betweenAddress = userData.betweenAddress
                )

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