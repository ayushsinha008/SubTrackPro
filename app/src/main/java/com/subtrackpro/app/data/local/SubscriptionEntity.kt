package com.subtrackpro.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val price: Double,
    val billingCycle: String,
    val startDate: Long,
    val nextBillingDate: Long,
    val reminderDays: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
