package com.subtrackpro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: SubscriptionRepository
) : ViewModel() {
    val sub = MutableStateFlow<SubscriptionEntity?>(null)
    fun load(id: String) = viewModelScope.launch { sub.value = repo.getById(id) }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }
}
