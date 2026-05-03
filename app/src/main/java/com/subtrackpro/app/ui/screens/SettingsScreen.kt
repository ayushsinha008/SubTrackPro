package com.subtrackpro.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {
    val ctx = LocalContext.current
    var dark by remember { mutableStateOf(vm.isDark()) }
    var currency by remember { mutableStateOf(vm.currency()) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            ListItem(
                headlineContent = { Text("Dark Mode") },
                trailingContent = { Switch(dark, { dark = it; vm.setDark(it) }) }
            )
            Divider()
            ExposedDropdownMenuBox(expanded, { expanded = it }) {
                OutlinedTextField(currency, {}, readOnly = true, label = { Text("Currency") },
                    modifier = Modifier.fillMaxWidth().menuAnchor())
                ExposedDropdownMenu(expanded, { expanded = false }) {
                    listOf("₹", "$", "€", "£").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            currency = it; vm.setCurrency(it); expanded = false
                        })
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button({ vm.export(ctx) }, Modifier.fillMaxWidth()) { Text("Export Backup") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton({ vm.import(ctx) }, Modifier.fillMaxWidth()) { Text("Import Backup") }
            Spacer(Modifier.height(16.dp))
            TextButton({ vm.clearAll() }, Modifier.fillMaxWidth(), colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                Text("Clear All Data")
            }
        }
    }
}
