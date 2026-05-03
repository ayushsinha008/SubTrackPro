package com.subtrackpro.app.data.repository

import com.subtrackpro.app.data.local.SubscriptionDao
import com.subtrackpro.app.data.local.SubscriptionEntity
import java.util.concurrent.TimeUnit

class SubscriptionRepositoryImpl(private val dao: SubscriptionDao) : SubscriptionRepository {
    override fun getAll() = dao.getAll()
    override fun getUpcoming(days: Int) = dao.getUpcoming(
        System.currentTimeMillis(),
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days.toLong())
    )
    override suspend fun getById(id: String) = dao.getById(id)
    override suspend fun upsert(sub: SubscriptionEntity) = dao.upsert(sub)
    override suspend fun delete(id: String) = dao.delete(id)
    override suspend fun getNextN(n: Int) = dao.getNextN(n)
    override suspend fun deleteAll() = dao.deleteAll()
    override suspend fun importAll(list: List<SubscriptionEntity>) = dao.upsertAll(list)
}
