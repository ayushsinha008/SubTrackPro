package com.subtrackpro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: HomeViewModel = hiltViewModel(),
               onAdd: () -> Unit, onItemClick: (String) -> Unit) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("SubTrack Pro") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Icon(Icons.Default.Add, "Add") }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Monthly Spend", style = MaterialTheme.typography.labelMedium)
                    Text("₹${"%.2f".format(state.totalMonthly)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(state.query, vm::setQuery,
                label = { Text("Search") }, modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
            Spacer(Modifier.height(12.dp))

            if (state.upcoming.isNotEmpty()) {
                Text("Upcoming (7 days)", style = MaterialTheme.typography.titleMedium)
                LazyColumn(Modifier.heightIn(max = 160.dp)) {
                    items(state.upcoming) { sub -> UpcomingItem(sub) { onItemClick(sub.id) } }
                }
                Spacer(Modifier.height(8.dp))
            }

            Text("All Subscriptions", style = MaterialTheme.typography.titleMedium)
            if (state.subs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No subscriptions yet.\nTap + to add one.")
                }
            } else {
                LazyColumn { items(state.subs) { sub ->
                    SubCard(sub) { onItemClick(sub.id) }
                } }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCard(sub: SubscriptionEntity, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(sub.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${sub.category} • ${sub.billingCycle}", style = MaterialTheme.typography.bodySmall)
                Text("Next: ${DateUtils.format(sub.nextBillingDate)}", style = MaterialTheme.typography.bodySmall)
            }
            Text("₹${"%.2f".format(sub.price)}", fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingItem(sub: SubscriptionEntity, onClick: () -> Unit) {
    val days = DateUtils.daysBetween(System.currentTimeMillis(), sub.nextBillingDate)
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Row(Modifier.padding(12.dp)) {
            Icon(Icons.Default.Notifications, null)
            Spacer(Modifier.width(8.dp))
            Text("${sub.name} • ${if (days==0) "today" else "in $days day(s)"} • ₹${"%.2f".format(sub.price)}")
        }
    }
}
