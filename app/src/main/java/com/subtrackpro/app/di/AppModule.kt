package com.subtrackpro.app.di

import android.content.Context
import androidx.room.Room
import com.subtrackpro.app.data.local.AppDatabase
import com.subtrackpro.app.data.local.SubscriptionDao
import com.subtrackpro.app.data.repository.SubscriptionRepository
import com.subtrackpro.app.data.repository.SubscriptionRepositoryImpl
import com.subtrackpro.app.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "subtrack.db")
            .fallbackToDestructiveMigration().build()

    @Provides fun provideDao(db: AppDatabase): SubscriptionDao = db.subscriptionDao()

    @Provides @Singleton
    fun providePrefs(@ApplicationContext ctx: Context) = PreferenceManager(ctx)

    @Provides @Singleton
    fun provideRepo(dao: SubscriptionDao): SubscriptionRepository =
        SubscriptionRepositoryImpl(dao)
}
