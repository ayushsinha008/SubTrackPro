package com.subtrackpro.app.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.data.repository.SubscriptionRepository
import com.subtrackpro.app.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferenceManager,
    private val repo: SubscriptionRepository
) : ViewModel() {
    fun isDark() = prefs.darkMode
    fun setDark(v: Boolean) { prefs.darkMode = v }
    fun currency() = prefs.currency
    fun setCurrency(v: String) { prefs.currency = v }

    fun export(ctx: Context) = viewModelScope.launch {
        val list = repo.getAll().first()
        val f = File(ctx.getExternalFilesDir(null), "subtrack_backup.json")
        f.writeText(Gson().toJson(list))
        Toast.makeText(ctx, "Saved: ${f.absolutePath}", Toast.LENGTH_LONG).show()
    }

    fun import(ctx: Context) = viewModelScope.launch {
        val f = File(ctx.getExternalFilesDir(null), "subtrack_backup.json")
        if (!f.exists()) { Toast.makeText(ctx, "No backup found", Toast.LENGTH_SHORT).show(); return@launch }
        val type = object : TypeToken<List<SubscriptionEntity>>() {}.type
        val list: List<SubscriptionEntity> = Gson().fromJson(f.readText(), type)
        repo.importAll(list)
        Toast.makeText(ctx, "Imported ${list.size} items", Toast.LENGTH_SHORT).show()
    }

    fun clearAll() = viewModelScope.launch { repo.deleteAll() }
}
