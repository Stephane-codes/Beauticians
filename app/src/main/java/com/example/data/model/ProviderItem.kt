package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class ProviderItem(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val specialty: String,
    val rating: Float,
    val reviewsCount: Int,
    val phone: String,
    val location: String,
    val subscriptionTier: String, // "Pro Monthly (KES 1,499)", "Weekly Starter (KES 499)"
    val isActive: Boolean = true,
    val bio: String = ""
)
