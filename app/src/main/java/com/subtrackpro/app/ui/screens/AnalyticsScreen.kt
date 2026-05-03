package com.subtrackpro.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.viewmodel.AnalyticsViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(vm: AnalyticsViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val colors = listOf(Color(0xFF6750A4), Color(0xFF03A9F4), Color(0xFF4CAF50),
        Color(0xFFFF9800), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF607D8B))

    Scaffold(topBar = { TopAppBar(title = { Text("Analytics") }) }) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Monthly Total", style = MaterialTheme.typography.labelMedium)
                    Text("₹${"%.2f".format(state.totalMonthly)}",
                        style = MaterialTheme.typography.headlineMedium)
                    Text("Yearly: ₹${"%.2f".format(state.totalYearly)}")
                    if (state.highSpend) {
                        Spacer(Modifier.height(8.dp))
                        Text("⚠️ High spending detected!", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("By Category", style = MaterialTheme.typography.titleMedium)
            Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val total = state.byCategory.values.sum().toFloat()
                    if (total > 0) {
                        var start = 0f
                        val d = min(size.width, size.height) * 0.8f
                        val tl = Offset((size.width - d) / 2, (size.height - d) / 2)
                        state.byCategory.entries.forEachIndexed { i, (_, v) ->
                            val sweep = (v.toFloat() / total) * 360f
                            drawArc(colors[i % colors.size], start, sweep, true, tl, Size(d, d))
                            start += sweep
                        }
                    }
                }
            }
            state.byCategory.entries.forEachIndexed { i, (c, v) ->
                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(colors[i % colors.size]))
                    Spacer(Modifier.width(8.dp))
                    Text(c, Modifier.weight(1f))
                    Text("₹${"%.2f".format(v)}")
                }
            }
        }
    }
}
