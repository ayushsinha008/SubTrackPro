package com.subtrackpro.app.widget

import android.content.Context
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.data.repository.SubscriptionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object WidgetDataLoader {
    @EntryPoint @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint { fun repository(): SubscriptionRepository }

    suspend fun load(ctx: Context): List<SubscriptionEntity> {
        val ep = EntryPointAccessors.fromApplication(ctx, WidgetEntryPoint::class.java)
        return ep.repository().getNextN(5)
    }
}
