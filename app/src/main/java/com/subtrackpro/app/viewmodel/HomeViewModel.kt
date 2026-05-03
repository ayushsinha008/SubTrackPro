package com.subtrackpro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val subs: List<SubscriptionEntity> = emptyList(),
    val upcoming: List<SubscriptionEntity> = emptyList(),
    val totalMonthly: Double = 0.0,
    val query: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: SubscriptionRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val state: StateFlow<HomeUiState> = combine(
        repo.getAll(), repo.getUpcoming(7), _query
    ) { all, upcoming, q ->
        val filtered = all.filter { q.isBlank() || it.name.contains(q, true) }
        HomeUiState(
            subs = filtered, upcoming = upcoming,
            totalMonthly = all.sumOf { if (it.billingCycle == "MONTHLY") it.price else it.price / 12 },
            query = q
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun setQuery(q: String) { _query.value = q }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }
}
