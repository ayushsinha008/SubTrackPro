package com.subtrackpro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.data.repository.SubscriptionRepository
import com.subtrackpro.app.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repo: SubscriptionRepository
) : ViewModel() {

    private val _sub = MutableStateFlow<SubscriptionEntity?>(null)
    val sub = _sub.asStateFlow()

    fun load(id: String?) = viewModelScope.launch {
        _sub.value = id?.let { repo.getById(it) }
    }

    fun save(id: String?, name: String, category: String, price: Double,
             cycle: String, startDate: Long, reminder: Int) = viewModelScope.launch {
        repo.upsert(SubscriptionEntity(
            id = id ?: UUID.randomUUID().toString(),
            name = name, category = category, price = price,
            billingCycle = cycle, startDate = startDate,
            nextBillingDate = DateUtils.calcNextBillingDate(startDate, cycle),
            reminderDays = reminder
        ))
    }
}
