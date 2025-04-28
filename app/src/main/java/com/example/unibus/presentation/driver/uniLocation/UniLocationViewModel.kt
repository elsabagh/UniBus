package com.example.unibus.presentation.driver.uniLocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniLocationViewModel@Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {
    private val _driver = MutableStateFlow<User?>(null)
    val driver: StateFlow<User?> = _driver

    private val _uniLatLng = MutableStateFlow<LatLng?>(null)
    val uniLatLng: StateFlow<LatLng?> = _uniLatLng

    fun loadUniLatLng() {
        viewModelScope.launch {
            val user = accountRepository.getCurrentUser()
            _driver.value = user

            user?.uniAddress?.let { address ->
                val parts = address.split(",")
                if (parts.size == 2) {
                    val lat = parts[0].trim().toDoubleOrNull()
                    val lng = parts[1].trim().toDoubleOrNull()
                    if (lat != null && lng != null) {
                        _uniLatLng.value = LatLng(lat, lng)
                    }
                }
            }
        }
    }


}