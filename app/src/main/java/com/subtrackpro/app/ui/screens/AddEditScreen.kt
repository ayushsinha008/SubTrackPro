package com.subtrackpro.app.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.domain.Categories
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.AddEditViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(id: String?, vm: AddEditViewModel = hiltViewModel(), onBack: () -> Unit) {
    val ctx = LocalContext.current
    LaunchedEffect(id) { vm.load(id) }
    val existing by vm.sub.collectAsState()

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("OTT") }
    var price by remember { mutableStateOf("") }
    var cycle by remember { mutableStateOf("MONTHLY") }
    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var reminder by remember { mutableStateOf(3) }
    var catExp by remember { mutableStateOf(false) }
    var cycExp by remember { mutableStateOf(false) }
    var remExp by remember { mutableStateOf(false) }

    LaunchedEffect(existing) {
        existing?.let {
            name = it.name; category = it.category; price = it.price.toString()
            cycle = it.billingCycle; startDate = it.startDate; reminder = it.reminderDays
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(if (id == null) "Add" else "Edit") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } })
    }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(catExp, { catExp = it }) {
                OutlinedTextField(category, {}, readOnly = true, label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExp) })
                ExposedDropdownMenu(catExp, { catExp = false }) {
                    Categories.all.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { category = it; catExp = false })
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(price, { price = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(cycExp, { cycExp = it }) {
                OutlinedTextField(cycle, {}, readOnly = true, label = { Text("Billing Cycle") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cycExp) })
                ExposedDropdownMenu(cycExp, { cycExp = false }) {
                    listOf("MONTHLY","YEARLY").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { cycle = it; cycExp = false })
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(DateUtils.format(startDate), {}, readOnly = true,
                label = { Text("Start Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = {
                        val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                        DatePickerDialog(ctx, { _, y, m, d ->
                            startDate = Calendar.getInstance().apply { set(y,m,d) }.timeInMillis
                        }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
                    }) { Text("Pick") }
                })
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(remExp, { remExp = it }) {
                OutlinedTextField("$reminder day(s) before", {}, readOnly = true,
                    label = { Text("Reminder") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(remExp) })
                ExposedDropdownMenu(remExp, { remExp = false }) {
                    listOf(1,3,7).forEach {
                        DropdownMenuItem(text = { Text("$it day(s)") }, onClick = { reminder = it; remExp = false })
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                val p = price.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank() && p > 0) {
                    vm.save(id, name, category, p, cycle, startDate, reminder); onBack()
                }
            }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
        }
    }
}
