package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingItem(
    @PrimaryKey val id: String,
    val clientName: String,
    val clientPhone: String,
    val clientEmail: String,
    val serviceId: String,
    val serviceName: String,
    val appointmentDate: String, // e.g. "2026-10-18"
    val timeSlot: String, // e.g. "10:00 AM"
    val isHouseCall: Boolean,
    val locationArea: String, // "Embu Town CBD", "Dallas", "Majengo", "Kangaru", etc.
    val specificAddress: String,
    val isFriendsDeal: Boolean,
    val friendName: String = "",
    val friendPhone: String = "",
    val basePriceKes: Int,
    val discountKes: Int,
    val transportFeeKes: Int,
    val totalAmountKes: Int,
    val depositAmountKes: Int,
    val isDepositPaid: Boolean,
    val paymentOption: String = "RETAINER_FEE_20", // "PAY_PRIOR_FULL", "RETAINER_FEE_20", "PAY_AFTER_SERVICE"
    val mpesaReference: String = "",
    val status: String = "Confirmed", // "Confirmed", "In Progress", "Completed", "Cancelled"
    val inspoDescription: String = "",
    val nailShape: String = "Almond",
    val nailLength: String = "Medium",
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
