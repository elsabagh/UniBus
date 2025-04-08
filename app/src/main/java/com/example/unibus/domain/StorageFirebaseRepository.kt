package com.example.unibus.domain

import com.example.unibus.data.models.User
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    fun getUserById(userId: String): Flow<User>

    suspend fun updateUserProfile(updatedUser: User)

    suspend fun getAvailableBuses(): List<User>

    suspend fun createBookBusRequest(user: User)

    suspend fun getBookingsForTrip(tripNo: String, status: String): List<User>

    suspend fun approveBooking(userId: String)

    suspend fun updateDriverSeatCount(driverBusId: String)

    suspend fun rejectBooking(userId: String)

    suspend fun clearTripBookings(tripNo: String)

    suspend fun resetDriverSeats(driverId: String)

}