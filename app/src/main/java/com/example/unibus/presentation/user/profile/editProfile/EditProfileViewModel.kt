package com.example.unibus.presentation.user.profile.editProfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.unibus.R
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import com.example.unibus.navigation.AppDestination
import com.example.unibus.utils.isPasswordValid
import com.example.unibus.utils.snackbar.SnackBarManager
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _editUserState = MutableStateFlow(EditUserState())
    val editUserState: StateFlow<EditUserState> = _editUserState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val password
        get() = _editUserState.value.password

    private val newPassword
        get() = _editUserState.value.newPassword

    fun onUserNameChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(userName = newValue)
    }

    fun onEmailChange(newEmail: String) {
        _editUserState.value = _editUserState.value.copy(email = newEmail)
    }

    fun onMobileChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(phoneNumber = newValue)
    }
    fun onIdNumberChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(idNumber = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(password = newValue)
    }

    fun onNewPasswordChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(newPassword = newValue)
    }

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                storageRepository.getUserById(userId).collect { fetchedUser ->
                    _editUserState.value = _editUserState.value.copy(
                        userId = fetchedUser.userId,
                        userName = fetchedUser.userName,
                        email = fetchedUser.email,
                        phoneNumber = fetchedUser.phoneNumber,
                        idNumber = fetchedUser.idNumber,
                        userPhoto = fetchedUser.userPhoto,
                    )
                }
            } catch (e: Exception) {
                _editUserState.value =
                    _editUserState.value.copy(errorMessage = "Failed to fetch user data")
            }
        }
    }

    fun updateUserEmail(
        currentPassword: String,
        newEmail: String,
        navController: NavController,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = accountRepository.updateUserEmail(currentPassword, newEmail)
            result.fold(
                onSuccess = {
                    navController.popBackStack()
                    navController.navigate(AppDestination.ProfileUserDestination.route) {
                        navController.popBackStack()
                    }

                    onSuccess()
                },
                onFailure = { e -> onFailure(e.message ?: "Failed to update email") }
            )
        }
    }




    fun updateUserProfile(navController: NavController, newUserPhotoUri: Uri?) {
        viewModelScope.launch {
            try {

                val newUserPhotoUrl = newUserPhotoUri?.let {
                    uploadImageToStorage(_editUserState.value.userId, it, "user_photo")
                } ?: _editUserState.value.userPhoto

                val updatedUser = User(
                    userId = _editUserState.value.userId,
                    userName = _editUserState.value.userName,
                    email = _editUserState.value.email,
                    phoneNumber = _editUserState.value.phoneNumber,
                    idNumber = _editUserState.value.idNumber,
                    userPhoto = newUserPhotoUrl,
                )

                storageRepository.updateUserProfile(updatedUser)

                _editUserState.value = _editUserState.value.copy(
                    userPhoto = newUserPhotoUrl,
                )

                navController.popBackStack()
                navController.navigate(AppDestination.ProfileUserDestination.route) {
                    navController.popBackStack()
                }

            } catch (e: Exception) {
                _editUserState.value = _editUserState.value.copy(errorMessage = "Failed to update profile")
            }
        }
    }


    private suspend fun uploadImageToStorage(userId: String, fileUri: Uri, fileName: String): String {
        return try {
            val ref = FirebaseStorage.getInstance().reference.child("users/$userId/$fileName.jpg")
            ref.putFile(fileUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error uploading image: $fileName", e)
            throw Exception("Failed to upload image", e)
        }
    }

    fun changePassword() {

        if (!password.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.invalid_password_error)
            return
        }

        if (!newPassword.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.invalid_password_error)
            return
        }

        viewModelScope.launch {
            _editUserState.value = _editUserState.value.copy(isLoading = true, error = null)
            try {
                accountRepository.changePassword(newPassword)
                _editUserState.value = _editUserState.value.copy(isPasswordChanged = true)
                SnackBarManager.showMessage(R.string.password_changed_successfully)
            } catch (e: Exception) {
                _editUserState.value =
                    _editUserState.value.copy(isLoading = false, error = e.message)
            } finally {
                _editUserState.value = _editUserState.value.copy(isLoading = false)
            }
        }
    }

}
