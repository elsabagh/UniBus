package com.example.unibus.presentation.user.profile.profileDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUserDetailsViewModel@Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isAccountSignedOut = MutableStateFlow(false)
    val isAccountSignedOut get() = _isAccountSignedOut

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val userId = accountRepository.currentUserId
        if (userId.isNotEmpty()) {
            storageRepository.getUserById(userId)
                .onEach { _user.value = it }
                .launchIn(viewModelScope)
        }
    }


    fun signOutFromAccount() {
        viewModelScope.launch {
            accountRepository.signOut()
            _isAccountSignedOut.value = true
        }
    }
}