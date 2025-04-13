package com.example.unibus.presentation.signUp


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.utils.isEmailValid
import com.example.unibus.utils.isPasswordValid
import com.example.unibus.utils.passwordMatches
import com.example.unibus.utils.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    private var _isAccountCreated = MutableStateFlow(false)
    val isAccountCreated: StateFlow<Boolean> = _isAccountCreated.asStateFlow()

    fun onUserNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(userName = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onMobileChange(newValue: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newValue)
    }

    fun onIdNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(idNumber = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue)
    }

    fun createAccount(userPhotoUri: Uri?) {
        val state = _uiState.value

        if (state.userName.isEmpty()) {
            SnackBarManager.showMessage(R.string.empty_name_error)
            return
        }

        if (!state.email.isEmailValid()) {
            SnackBarManager.showMessage(R.string.email_error)
            return
        }

        if (state.password.isEmpty()) {
            SnackBarManager.showMessage(R.string.empty_password_error)
            return
        }

        if (!state.password.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.password_error)
            return
        }

        if (!state.password.passwordMatches(state.confirmPassword)) {
            SnackBarManager.showMessage(R.string.password_match_error)
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val user = User(
                    userName = state.userName,
                    email = state.email,
                    phoneNumber = state.phoneNumber,
                    idNumber = state.idNumber,
                    addressMaps = state.addressMaps,
                    timeTrip = state.timeTrip,
                    dateTrip = state.dateTrip,
                    role = "user",
                )

                accountRepository.createAccount(
                    email = state.email,
                    password = state.password,
                    userData = user,
                    userPhotoUri = userPhotoUri
                )
                accountRepository.signOut()


                _isAccountCreated.value = true
            } catch (e: Exception) {
                if (e.message?.contains(R.string.email_already_in_use.toString()) == true) {
                    SnackBarManager.showMessage(R.string.email_error)
                } else {
                    SnackBarManager.showMessage(R.string.account_creation_failed)
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun resetIsAccountCreated() {
        _isAccountCreated.value = false
    }
}
