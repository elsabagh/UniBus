package com.example.unibus.domain

import android.net.Uri
import com.example.unibus.data.models.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val currentUser: Flow<User>

    val currentUserId: String

    val isUserSignedIn: Boolean

    suspend fun authenticate(email: String, password: String)

    suspend fun createAccount(
        email: String,
        password: String,
        userData: User,
        userPhotoUri: Uri?,
    )

    suspend fun getCurrentUserEmail(): String?

    suspend fun updateUserEmail(currentPassword: String, newEmail: String): Result<Unit>

    suspend fun getCurrentUser(): User?

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun linkAccount(email: String, password: String)

    suspend fun deleteAccount()

    suspend fun signOut()

}