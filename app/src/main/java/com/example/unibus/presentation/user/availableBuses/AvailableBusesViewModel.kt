package com.example.unibus.presentation.user.availableBuses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.data.repository.StorageFirebaseRepositoryImpl
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.GraphHopperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableBusesViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageFirebaseRepository: StorageFirebaseRepositoryImpl,
    private val graphHopperRepository: GraphHopperRepository

) : ViewModel() {

    private val _state = MutableStateFlow(AvailableBusesState())
    val state: StateFlow<AvailableBusesState> = _state.asStateFlow()

    private val _drivers = MutableStateFlow<List<User>>(emptyList())
    val drivers: StateFlow<List<User>> = _drivers.asStateFlow()

    private val _selectedBus = MutableStateFlow<User?>(null)
    val selectedBus: StateFlow<User?> = _selectedBus.asStateFlow()

    private val _driversWithDistance = MutableStateFlow<List<Pair<User, Double>>>(emptyList())
    val driversWithDistance: StateFlow<List<Pair<User, Double>>> = _driversWithDistance.asStateFlow()

    private val _userBetweenAddress = MutableStateFlow(0.0)
    val userBetweenAddress: StateFlow<Double> = _userBetweenAddress.asStateFlow()

    fun getAvailableBuses() {
        viewModelScope.launch {
            _drivers.value = storageFirebaseRepository.getAvailableBuses()

            val currentUser = accountRepository.getCurrentUser()
            currentUser?.let {
                _userBetweenAddress.value = it.betweenAddress.toDoubleOrNull() ?: 0.0

                val driversWithDistance = mutableListOf<Pair<User, Double>>()

                _drivers.value.forEach { driver ->
                    val distance = getDistance(it.addressMaps, driver.addressMaps)
                    driversWithDistance.add(Pair(driver, distance))
                }

                _driversWithDistance.value = driversWithDistance.sortedBy { it.second }
            }
        }
    }


    private suspend fun getDistance(start: String, end: String): Double {
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

//    private fun logDistances(driversWithDistance: List<Pair<User, Double>>) {
//        driversWithDistance.forEachIndexed { index, pair ->
//            Log.d("AvailableBusesViewModel", "Driver ${index + 1}: ${pair.first.userName} - Distance: ${pair.second} meters")
//        }
//    }
    fun selectBus(driver: User) {
        _selectedBus.value = driver
    }


    fun bookBus(user: User) {
        viewModelScope.launch {
            _selectedBus.value?.let { bus ->
                val currentUser = accountRepository.getCurrentUser()
                currentUser?.let {
                    val fixedUserAddress = "30.026549, 31.211378"
                    val distance = getDistance(fixedUserAddress, bus.addressMaps)

                    storageFirebaseRepository.createBookBusRequest(
                        currentUser.copy(
                            driverBusId = bus.userId,
                            driverBusName = bus.userName,
                            tripNo = bus.tripNo,
                            statusBook = "processing",
                            addressMaps = user.addressMaps,
                            busPrice = user.busPrice,
                            betweenAddress = "$distance m"
                        )
                    )
                }
            }
        }
    }


}