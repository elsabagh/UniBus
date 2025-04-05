package com.example.unibus.presentation.user.availableBuses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.data.repository.StorageFirebaseRepositoryImpl
import com.example.unibus.domain.AccountRepository
import com.example.unibus.presentation.user.newTrip.NewTripState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableBusesViewModel@Inject constructor(
     private val accountRepository: AccountRepository,
     private val storageFirebaseRepository: StorageFirebaseRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(AvailableBusesState())
    val state: StateFlow<AvailableBusesState> = _state.asStateFlow()

    private val _drivers = MutableStateFlow<List<User>>(emptyList())
    val drivers: StateFlow<List<User>> = _drivers.asStateFlow()

    fun getAvailableBuses() {
        viewModelScope.launch {
            _drivers.value = storageFirebaseRepository.getAvailableBuses()
        }
    }


}