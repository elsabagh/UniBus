package com.example.unibus.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.ui.util.trace
import com.example.unibus.data.models.User
import com.example.unibus.domain.AccountRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AccountRepository {
    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(
                    auth.currentUser?.let {
                        User(
                            userId = it.uid,
                        )
                    } ?: User()
                )
            }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(listener)
            }
        }

    override val currentUserId: String
        get() = firebaseAuth.currentUser?.uid.orEmpty()

    override val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null


    override suspend fun authenticate(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account creation: ${e.message}", e)
        }
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        userData: User,
        userPhotoUri: Uri?
    ) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")
            val userPhotoUrl = userPhotoUri?.let { uploadImageToStorage(userId, it, "user_photo") }

            val user = userData.copy(
                userId = userId,
                userPhoto = userPhotoUrl.orEmpty()
            )
            fireStore.collection("users").document(userId).set(user).await()

        } catch (e: Exception) {
            throw Exception("Failed to create account: ${e.message}", e)
        }
    }

    override suspend fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    override suspend fun updateUserEmail(currentPassword: String, newEmail: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("No authenticated user found"))

        return try {
            // إعادة التحقق من هوية المستخدم
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()

            // تحديث الإيميل في Firebase Auth
            user.updateEmail(newEmail).await()

            // تحديث الإيميل في Firestore
            fireStore.collection("users").document(user.uid)
                .update("email", newEmail).await()

            // إعادة تسجيل الدخول لضمان استمرارية الجلسة
            firebaseAuth.signOut()
            firebaseAuth.signInWithEmailAndPassword(newEmail, currentPassword).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getCurrentUser(): User? {
        val userId = firebaseAuth.currentUser?.uid ?: return null
        return try {
            val userDoc = fireStore.collection("users").document(userId).get().await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching current user: ${e.message}")
            null
        }
    }

    override suspend fun changePassword(newPassword: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            try {
                user.updatePassword(newPassword).await()
            } catch (e: Exception) {
                throw Exception("Failed to change password: ${e.message}", e)
            } catch (e: IOException) {
                throw IOException("Network error occurred during password change: ${e.message}", e)
            }
        } else {
            throw Exception("No authenticated user found")
        }
    }

    private suspend fun uploadImageToStorage(
        userId: String,
        fileUri: Uri,
        fileName: String
    ): String {
        return try {
            val ref = storage.reference.child("users/$userId/$fileName.jpg")
            ref.putFile(fileUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error uploading image: $fileName", e)
            throw Exception("Failed to link account: ${e.message}", e)
        }
    }


    override suspend fun linkAccount(email: String, password: String) {
        try {
            trace(LINK_ACCOUNT_TRACE) {
                val credential = EmailAuthProvider.getCredential(email, password)
                firebaseAuth.currentUser!!.linkWithCredential(credential).await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to link account: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account linking: ${e.message}", e)
        }
    }

    override suspend fun deleteAccount() {
        try {
            firebaseAuth.currentUser!!.delete().await()
        } catch (e: Exception) {
            throw Exception("Failed to delete account: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account deletion: ${e.message}", e)
        }
    }

    override suspend fun signOut() {
        try {
            if (firebaseAuth.currentUser!!.isAnonymous) {
                firebaseAuth.currentUser!!.delete()
            }
            firebaseAuth.signOut()
        } catch (e: Exception) {
            throw Exception("Failed to sign out: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during sign-out: ${e.message}", e)
        }
    }

    // حذف الصور من Firebase Storage
    private suspend fun deleteUserImagesFromStorage(userId: String) {
        try {
            // تحديد مسارات الصور المرتبطة بالحساب
            val userPhotoRef = storage.reference.child("users/$userId/user_photo.jpg")

            try {
                userPhotoRef.delete().await()
                Log.d("FirebaseStorage", "User photo image deleted successfully.")
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "Error deleting User photo image: ${e.message}")
            }

        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error deleting user images: ${e.message}")
        }
    }


    companion object {
        const val LINK_ACCOUNT_TRACE = "linkAccount"

    }

}