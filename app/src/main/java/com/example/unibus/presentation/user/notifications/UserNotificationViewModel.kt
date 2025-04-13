package com.example.unibus.presentation.user.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unibus.data.models.Notification
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import com.example.unibus.utils.NotificationUtil
import com.example.unibus.utils.UserNotificationPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNotificationViewModel @Inject constructor(
    private val storageRepository: StorageFirebaseRepository,
    private val accountRepository: AccountRepository,
    context: Context
) : ViewModel() {

    private val _stateNotifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _stateNotifications.asStateFlow()

    private val _hasNotifications = MutableStateFlow(false)
    val hasNotifications: StateFlow<Boolean> = _hasNotifications

    val userId = accountRepository.currentUserId

    init {
        loadNotifications(
            context
        )
    }

    fun loadNotifications(context: Context) {
        viewModelScope.launch {
            val userId = accountRepository.currentUserId ?: ""

            // جلب الإشعارات لهذا المستخدم من قاعدة البيانات
            val userNotificationWithDocId = storageRepository.getNotificationsForUser(userId)

            // استخراج معرّفات documentId من الإشعارات
            val currentDocumentIds = userNotificationWithDocId.mapNotNull { it.documentId }.toSet()
            val notifiedIds = UserNotificationPrefs.getNotifiedIds(context)

            // تحديد المعرفات الجديدة (documentId) التي لم يتم إشعارها بعد
            val newDocumentIds = currentDocumentIds.subtract(notifiedIds)

            if (newDocumentIds.isNotEmpty()) {
                // إرسال إشعار إذا كانت هناك إشعارات جديدة
                NotificationUtil.showNotification(
                    context,
                    "There are ${newDocumentIds.size} NEW Notifications.",
                    "Click to view"
                )

                // حفظ المعرفات المحدثة
                UserNotificationPrefs.saveNotifiedIds(context, currentDocumentIds)
            }

            // تعيين حالة الإشعارات للمستخدم
            _stateNotifications.value = userNotificationWithDocId.map { it.notification }
            _hasNotifications.value = userNotificationWithDocId.isNotEmpty()
        }
    }


    fun markNotificationsAsRead() {
        _hasNotifications.value = false
    }
}