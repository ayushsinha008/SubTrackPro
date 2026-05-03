package com.subtrackpro.app.utils

import android.content.Context

class PreferenceManager(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("subtrack_prefs", Context.MODE_PRIVATE)
    var darkMode: Boolean
        get() = prefs.getBoolean("dark", false)
        set(v) = prefs.edit().putBoolean("dark", v).apply()
    var currency: String
        get() = prefs.getString("currency", "₹") ?: "₹"
        set(v) = prefs.edit().putString("currency", v).apply()
}
