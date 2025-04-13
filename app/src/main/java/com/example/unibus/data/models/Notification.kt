package com.example.unibus.data.models

data class Notification(
    val title: String = "",
    val message: String = "",
    val date: String = "",
    val time: String = "",
    val userId: String = "",
    val userName: String = "",
    val nameDriver: String = "",
    val notificationType: String = "",
    val addressMaps: String = "",
    val documentId: String = ""
)

data class NotificationWithDocId(
    val notification: Notification,
    val documentId: String
)