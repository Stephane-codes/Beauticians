package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isUrgent: Boolean = false,
    val isRead: Boolean = false,
    val type: String = "SCHEDULING" // "SCHEDULING", "PAYMENT", "REMINDER", "PROMO"
)
