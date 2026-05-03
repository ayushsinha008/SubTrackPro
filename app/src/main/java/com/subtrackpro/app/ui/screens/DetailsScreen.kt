package com.subtrackpro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(id: String, vm: DetailsViewModel = hiltViewModel(),
                  onEdit: (String) -> Unit, onBack: () -> Unit) {
    LaunchedEffect(id) { vm.load(id) }
    val sub by vm.sub.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text("Details") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
            actions = {
                IconButton(onClick = { onEdit(id) }) { Icon(Icons.Default.Edit, null) }
                IconButton(onClick = { vm.delete(id); onBack() }) { Icon(Icons.Default.Delete, null) }
            })
    }) { pad ->
        sub?.let { s ->
            Column(Modifier.padding(pad).padding(20.dp)) {
                Text(s.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(12.dp))
                Info("Category", s.category)
                Info("Price", "₹${"%.2f".format(s.price)}")
                Info("Cycle", s.billingCycle)
                Info("Start", DateUtils.format(s.startDate))
                Info("Next Billing", DateUtils.format(s.nextBillingDate))
                Info("Reminder", "${s.reminderDays} day(s) before")
            }
        }
    }
}

@Composable
private fun Info(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
