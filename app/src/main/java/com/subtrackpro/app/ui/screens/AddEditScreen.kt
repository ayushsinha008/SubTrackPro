package com.subtrackpro.app.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.domain.Categories
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.AddEditViewModel
import com.subtrackpro.app.ui.theme.glassMorphic
import com.subtrackpro.app.ui.theme.GlassBorder
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

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
        TopAppBar(
            title = { Text(if (id == null) "Add Subscription" else "Edit Subscription", color = Color.White) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp).verticalScroll(rememberScrollState())) {
            
            if (id == null) {
                Text("Quick Add", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(PrebuiltSubscriptions) { prebuilt ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(prebuilt.color.copy(alpha = 0.8f))
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    name = prebuilt.name
                                    category = prebuilt.category
                                    price = prebuilt.defaultPrice.toString()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(prebuilt.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassMorphic(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name, 
                        onValueChange = { name = it }, 
                        label = { Text("Name", color = Color.White.copy(alpha = 0.7f)) }, 
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder)
                    )
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(catExp, { catExp = it }) {
                        OutlinedTextField(category, {}, readOnly = true, label = { Text("Category", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExp) },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder)
                        )
                        ExposedDropdownMenu(catExp, { catExp = false }, modifier = Modifier.background(Color(0xFF243B55))) {
                            Categories.all.forEach {
                                DropdownMenuItem(text = { Text(it, color = Color.White) }, onClick = { category = it; catExp = false })
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(price, { price = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Price", color = Color.White.copy(alpha = 0.7f)) }, modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder))
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(cycExp, { cycExp = it }) {
                        OutlinedTextField(cycle, {}, readOnly = true, label = { Text("Billing Cycle", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cycExp) },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder))
                        ExposedDropdownMenu(cycExp, { cycExp = false }, modifier = Modifier.background(Color(0xFF243B55))) {
                            listOf("MONTHLY","YEARLY").forEach {
                                DropdownMenuItem(text = { Text(it, color = Color.White) }, onClick = { cycle = it; cycExp = false })
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(DateUtils.format(startDate), {}, readOnly = true,
                        label = { Text("Start Date", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder),
                        trailingIcon = {
                            TextButton(onClick = {
                                val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                                DatePickerDialog(ctx, { _, y, m, d ->
                                    startDate = Calendar.getInstance().apply { set(y,m,d) }.timeInMillis
                                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
                            }) { Text("Pick", color = MaterialTheme.colorScheme.primary) }
                        })
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(remExp, { remExp = it }) {
                        OutlinedTextField("$reminder day(s) before", {}, readOnly = true,
                            label = { Text("Reminder", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(remExp) },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = GlassBorder))
                        ExposedDropdownMenu(remExp, { remExp = false }, modifier = Modifier.background(Color(0xFF243B55))) {
                            listOf(1,3,7).forEach {
                                DropdownMenuItem(text = { Text("$it day(s)", color = Color.White) }, onClick = { reminder = it; remExp = false })
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                val p = price.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank() && p > 0) {
                    vm.save(id, name, category, p, cycle, startDate, reminder); onBack()
                }
            }, modifier = Modifier.fillMaxWidth(),
               colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Save", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)) }
        }
    }
}
