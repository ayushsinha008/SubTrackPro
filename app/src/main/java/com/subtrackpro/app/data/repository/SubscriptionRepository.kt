package com.subtrackpro.app.data.repository

import com.subtrackpro.app.data.local.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getAll(): Flow<List<SubscriptionEntity>>
    fun getUpcoming(days: Int): Flow<List<SubscriptionEntity>>
    suspend fun getById(id: String): SubscriptionEntity?
    suspend fun upsert(sub: SubscriptionEntity)
    suspend fun delete(id: String)
    suspend fun getNextN(n: Int): List<SubscriptionEntity>
    suspend fun deleteAll()
    suspend fun importAll(list: List<SubscriptionEntity>)
}
