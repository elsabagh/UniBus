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

    override suspend fun createBookBusRequest(user: User) {
        try {
            val db = FirebaseFirestore.getInstance()
            val bookedBusData = mapOf(
                "userId" to user.userId,
                "userName" to user.userName,
                "email" to user.email,
                "phoneNumber" to user.phoneNumber,
                "userPhoto" to user.userPhoto,
                "idNumber" to user.idNumber,
                "driverBusId" to user.driverBusId,
                "driverBusName" to user.driverBusName,
                "busPrice" to user.busPrice,
                "tripNo" to user.tripNo,
                "bookedDate" to user.bookedDate,
                "bookedTime" to user.bookedTime,
                "statusBook" to user.statusBook,
                "bookedUserId" to user.bookedUserId,
                "addressMaps" to user.addressMaps,
                "betweenAddress" to user.betweenAddress,
                "latitude" to user.latitude,
                "longitude" to user.longitude,
                "statusBook" to user.statusBook,
            )

            db.collection("bookedBus")
                .add(bookedBusData)
                .await()

            Log.d("BookBusRequest", "Urgent Help Request created successfully.")

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error creating book bus request", e)
            throw e
        }
    }

    override suspend fun getBookingsForTrip(tripNo: String, status: String): List<User> {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("bookedBus")
                .whereEqualTo("tripNo", tripNo)
                .whereEqualTo("statusBook", status)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(User::class.java)?.copy(userId = it.id) }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching bookings", e)
            emptyList()
        }
    }

    override suspend fun approveBooking(userId: String) {
        try {
            FirebaseFirestore.getInstance().collection("bookedBus")
                .document(userId)
                .update("statusBook", "approved")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error approving booking", e)
        }
    }

    override suspend fun rejectBooking(userId: String) {
        try {
            FirebaseFirestore.getInstance().collection("bookedBus")
                .document(userId)
                .delete()
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error rejecting booking", e)
        }
    }

}