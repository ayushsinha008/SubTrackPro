package com.subtrackpro.app.ui.screens

import androidx.compose.ui.graphics.Color

data class PrebuiltSub(
    val name: String,
    val category: String,
    val defaultPrice: Double,
    val color: Color
)

val PrebuiltSubscriptions = listOf(
    PrebuiltSub("Netflix", "OTT", 199.0, Color(0xFFE50914)),
    PrebuiltSub("Spotify", "Music", 119.0, Color(0xFF1DB954)),
    PrebuiltSub("Prime Video", "OTT", 1499.0, Color(0xFF00A8E1)),
    PrebuiltSub("YouTube Premium", "OTT", 129.0, Color(0xFFFF0000)),
    PrebuiltSub("Apple Music", "Music", 99.0, Color(0xFFFC3C44))
)
