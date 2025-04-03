package com.example.unibus.domain

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?


}