package com.example.unibus.domain

import com.example.unibus.data.models.Notification
import com.example.unibus.data.models.NotificationWithDocId
import com.example.unibus.data.models.User
import com.example.unibus.data.models.UserWithDocId
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    fun getUserById(userId: String): Flow<User>

    suspend fun updateUserProfile(updatedUser: User)

    suspend fun updateUserTrip(updatedUser: User)

    suspend fun getAvailableBuses(): List<User>

    suspend fun createBookBusRequest(user: User)

    suspend fun getBookingsForTrip(tripNo: String, status: String): List<User>

    suspend fun createUserNotification(notification: Notification)

    suspend fun getBookingsForTripNot(tripNo: String, status: String): List<UserWithDocId>

    suspend fun approveBooking(userId: String)

    suspend fun updateDriverSeatCount(driverBusId: String)

    suspend fun rejectBooking(userId: String)

    suspend fun clearTripBookings(tripNo: String)

    suspend fun resetDriverSeats(driverId: String)

    suspend fun getBookingsForUser(userId: String, status: String): List<User>

    suspend fun getBookedBusForUser(userId: String): User?

    suspend fun getNotificationsForUser(userId: String): List<NotificationWithDocId>
}