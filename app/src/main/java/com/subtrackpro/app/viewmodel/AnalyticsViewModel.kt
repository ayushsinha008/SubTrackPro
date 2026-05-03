package com.subtrackpro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subtrackpro.app.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AnalyticsState(
    val byCategory: Map<String, Double> = emptyMap(),
    val totalMonthly: Double = 0.0,
    val totalYearly: Double = 0.0,
    val highSpend: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(repo: SubscriptionRepository) : ViewModel() {
    val state = repo.getAll().map { list ->
        val byCat = list.groupBy { it.category }.mapValues { (_, v) ->
            v.sumOf { if (it.billingCycle == "MONTHLY") it.price else it.price / 12 }
        }
        val m = byCat.values.sum()
        AnalyticsState(byCat, m, m * 12, m > 1000)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsState())
}
