package com.subtrackpro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(vm: HomeViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val grouped = state.subs.groupBy { DateUtils.format(it.nextBillingDate, "MMMM yyyy") }

    Scaffold(topBar = { TopAppBar(title = { Text("Renewals") }) }) { pad ->
        LazyColumn(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            grouped.forEach { (month, list) ->
                item {
                    Text(month, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                }
                items(list) { sub ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(Modifier.padding(16.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(sub.name, style = MaterialTheme.typography.titleSmall)
                                Text(DateUtils.format(sub.nextBillingDate, "EEE, dd MMM"))
                            }
                            Text("₹${"%.2f".format(sub.price)}")
                        }
                    }
                }
            }
        }
    }
}
