package com.example.unibus.presentation.user.newTrip

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NewTripViewModel @Inject constructor(
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
}