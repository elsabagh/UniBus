package com.example.unibus.domain

import com.example.unibus.data.models.User
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    fun getUserById(userId: String): Flow<User>

    suspend fun updateUserProfile(updatedUser: User)

}