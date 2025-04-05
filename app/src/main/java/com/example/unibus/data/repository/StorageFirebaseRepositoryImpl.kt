package com.example.unibus.data.repository


import android.util.Log
import com.example.unibus.data.models.User
import com.example.unibus.domain.StorageFirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageFirebaseRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : StorageFirebaseRepository {

    override suspend fun getUserRole(email: String): String? {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val role = if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].getString("role")
            } else {
                null
            }
            Log.d("FirebaseRepo", "User $email role: $role")
            role
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching user role", e)
            null
        }
    }

    override fun getUserById(userId: String): Flow<User> {
        return flow {
            val userDoc = fireStore.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)
            emit(user ?: User())
        }
    }

    override suspend fun updateUserProfile(updatedUser: User) {
        try {
            val userRef = fireStore.collection("users").document(updatedUser.userId)

            userRef.update(
                mapOf(
                    "userName" to updatedUser.userName,
                    "email" to updatedUser.email,
                    "mobile" to updatedUser.phoneNumber,
                    "userPhoto" to updatedUser.userPhoto,
                    "idNumber" to updatedUser.idNumber,
                )
            ).await()
        } catch (e: Exception) {
            Log.e("ProfileDetailsViewModel", "Failed to update profile: ${e.message}")
            throw e
        }
    }

    override suspend fun getAvailableBuses(): List<User> {
        return try {
            val snapshot = fireStore.collection("users")
                .whereEqualTo("role", "driver")
                .whereEqualTo("available", "available")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(User::class.java) }

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching available buses", e)
            emptyList()
        }
    }

}