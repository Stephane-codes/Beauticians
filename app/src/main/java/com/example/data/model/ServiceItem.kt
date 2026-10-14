package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceItem(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "Nails", "House Call Special", "Bridal & Ruracio", "Spa & Care", "Partner Specialist"
    val priceKes: Int,
    val durationMinutes: Int,
    val description: String,
    val isHouseCallEligible: Boolean = true,
    val isBridalPackage: Boolean = false,
    val isFriendsDealEligible: Boolean = true,
    val imageResName: String = "",
    val popularTag: String? = null
)
