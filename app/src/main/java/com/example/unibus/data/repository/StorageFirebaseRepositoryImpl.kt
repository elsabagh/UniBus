package com.example.unibus.data.repository


import android.util.Log
import com.example.unibus.data.models.Notification
import com.example.unibus.data.models.NotificationWithDocId
import com.example.unibus.data.models.User
import com.example.unibus.data.models.UserWithDocId
import com.example.unibus.domain.StorageFirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
                    "phoneNumber" to updatedUser.phoneNumber,
                    "userPhoto" to updatedUser.userPhoto,
                    "idNumber" to updatedUser.idNumber,
                )
            ).await()
        } catch (e: Exception) {
            Log.e("ProfileDetailsViewModel", "Failed to update profile: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUserTrip(updatedUser: User) {
        try {
            val userRef = fireStore.collection("users").document(updatedUser.userId)

            userRef.update(
                mapOf(
                    "addressMaps" to updatedUser.addressMaps,
                    "dateTrip" to updatedUser.dateTrip,
                    "timeTrip" to updatedUser.timeTrip,
                    "betweenAddress" to updatedUser.betweenAddress,
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

            snapshot.documents.mapNotNull { doc ->
                val user = doc.toObject(User::class.java)
                user?.copy(userId = user.userId)
            }        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching bookings", e)
            emptyList()
        }
    }

    override suspend fun getBookingsForTripNot(tripNo: String, status: String): List<UserWithDocId> {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("bookedBus")
                .whereEqualTo("tripNo", tripNo)
                .whereEqualTo("statusBook", status)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val user = doc.toObject(User::class.java)
                user?.let {
                    UserWithDocId(it, doc.id)
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching bookings", e)
            emptyList()
        }
    }


    override suspend fun createUserNotification(notification: Notification) {
        try {
            val notificationData = mapOf(
                "title" to notification.title,
                "message" to notification.message,
                "date" to notification.date,
                "time" to notification.time,
                "userId" to notification.userId,
                "userName" to notification.userName,
                "nameDriver" to notification.nameDriver,
                "notificationType" to notification.notificationType,
                "addressMaps" to notification.addressMaps,

            )
            FirebaseFirestore.getInstance()
                .collection("notification")
                .add(notificationData)
                .await()
            Log.d("NotificationRepo", "Notification created successfully.")
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error creating notification: ${e.message}")
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

    override suspend fun updateDriverSeatCount(driverBusId: String) {
        try {
            val db = FirebaseFirestore.getInstance()
            val driverRef = db.collection("users").document(driverBusId)
            val driverSnapshot = driverRef.get().await()

            val currentReserved = driverSnapshot.getString("reservedSeats")?.toIntOrNull() ?: 0
            val currentAvailable = driverSnapshot.getString("availableSeats")?.toIntOrNull() ?: 0

            driverRef.update(
                mapOf(
                    "reservedSeats" to (currentReserved + 1).toString(),
                    "availableSeats" to (currentAvailable - 1).coerceAtLeast(0).toString()
                )
            ).await()

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating driver seat count", e)
            throw e
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

    override suspend fun clearTripBookings(tripNo: String) {
        try {
            val db = FirebaseFirestore.getInstance()
            val snapshot = db.collection("bookedBus")
                .whereEqualTo("tripNo", tripNo)
                .whereEqualTo("statusBook", "approved")
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                // Extract user information from the document
                val userId = doc.getString("userId") ?: return@forEach
                val userName = doc.getString("userName") ?: "User"

                // Create a notification for the user
                val notification = Notification(
                    title = "The bus has arrived",
                    message = "The bus has arrived at the university, You can pay now.",
                    date = getCurrentDate(), // Helper to fetch current date
                    time = getCurrentTime(), // Helper to fetch current time
                    userId = userId,
                    userName = userName,
                    nameDriver = "", // Optionally include the driver's name if available
                    notificationType = "payment",
                    addressMaps = doc.getString("addressMaps") ?: ""
                )

                // Send the notification
                createUserNotification(notification)

                // Delete the document
                doc.reference.delete().await()
            }

            Log.d("FirebaseRepo", "All approved bookings deleted for trip $tripNo")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error clearing trip bookings", e)
            throw e
        }
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }

    override suspend fun resetDriverSeats(driverId: String) {
        try {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(driverId)
                .update(
                    mapOf(
                        "reservedSeats" to "0",
                        "availableSeats" to "42"
                    )
                ).await()

            Log.d("FirebaseRepo", "Driver seats reset to 0/42")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error resetting seats", e)
            throw e
        }
    }

    override suspend fun getBookingsForUser(userId: String, status: String): List<User> {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("bookedBus")
                .whereEqualTo("userId", userId)
                .whereEqualTo("statusBook", status)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching approved bookings", e)
            emptyList()
        }
    }
    override suspend fun getBookedBusForUser(userId: String): User? {
        return try {
            // البحث في مجموعة "bookedBus" عن السائق الذي يملك driverBusId المتطابق مع userId
            val snapshot = fireStore.collection("bookedBus")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // إذا كان يوجد حجز لهذا المستخدم، نعيد تفاصيل السائق
            if (!snapshot.isEmpty) {
                val bookedBusData = snapshot.documents[0]
                val driverBusId = bookedBusData.getString("driverBusId") ?: return null

                // الآن نبحث في مجموعة "users" للحصول على السائق الذي يتطابق مع driverBusId
                val driverSnapshot = fireStore.collection("users")
                    .whereEqualTo("userId", driverBusId)
                    .get()
                    .await()

                // إرجاع بيانات السائق إذا تم العثور عليها
                return if (!driverSnapshot.isEmpty) {
                    driverSnapshot.documents[0].toObject(User::class.java)
                } else {
                    null
                }
            }
            null
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching booked bus for user", e)
            null
        }
    }

    override suspend fun getNotificationsForUser(userId: String): List<NotificationWithDocId> {
        return try {
            val snapshot = fireStore.collection("notification")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // استخراج documentId مع الإشعار
            snapshot.documents.mapNotNull { doc ->
                val notification = doc.toObject(Notification::class.java)
                notification?.let {
                    // دمج الإشعار مع documentId
                    NotificationWithDocId(it, doc.id)
                }
            }

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching notifications for user", e)
            emptyList()
        }
    }



}