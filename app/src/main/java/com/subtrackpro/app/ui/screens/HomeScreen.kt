package com.subtrackpro.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.utils.DateUtils
import com.subtrackpro.app.viewmodel.HomeViewModel
import com.subtrackpro.app.ui.theme.glassMorphic
import com.subtrackpro.app.ui.theme.GlassBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: HomeViewModel = hiltViewModel(),
               onAdd: () -> Unit, onItemClick: (String) -> Unit) {
    val state by vm.state.collectAsState()

    Scaffold(
        containerColor = Color.Transparent, // Let the theme gradient show through
        topBar = { 
            TopAppBar(
                title = { Text("SubTrack Pro", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(1.dp, GlassBorder, CircleShape)
                            .clickable { /* TBD: Profile Action */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User Profile", tint = Color.White)
                    }
                }
            ) 
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) { Icon(Icons.Default.Add, "Add") }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            // Glass Morphic Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassMorphic(Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text("Monthly Spend", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("₹${"%.2f".format(state.totalMonthly)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
            }
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = state.query, 
                onValueChange = vm::setQuery,
                label = { Text("Search", color = Color.White.copy(alpha = 0.6f)) }, 
                modifier = Modifier.fillMaxWidth().glassMorphic(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.White) }, 
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.height(20.dp))

            if (state.upcoming.isNotEmpty()) {
                Text("Upcoming (7 days)", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(Modifier.height(8.dp))
                LazyColumn(Modifier.heightIn(max = 160.dp)) {
                    items(state.upcoming) { sub -> UpcomingItem(sub) { onItemClick(sub.id) } }
                }
                Spacer(Modifier.height(16.dp))
            }

            Text("All Subscriptions", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(Modifier.height(8.dp))
            if (state.subs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No subscriptions yet.\nTap + to add one.", color = Color.White.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) { 
                    items(state.subs) { sub ->
                        SubCard(sub) { onItemClick(sub.id) }
                    } 
                }
            }
        }
    }
}

@Composable
fun SubCard(sub: SubscriptionEntity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .glassMorphic(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(sub.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(sub.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text("${sub.category} • ${sub.billingCycle}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                Text("Next: ${DateUtils.format(sub.nextBillingDate)}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
            }
            Text("₹${"%.2f".format(sub.price)}", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun UpcomingItem(sub: SubscriptionEntity, onClick: () -> Unit) {
    val days = DateUtils.daysBetween(System.currentTimeMillis(), sub.nextBillingDate)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .glassMorphic(Color.Red.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, null, tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Text("${sub.name} • ${if (days==0) "today" else "in $days day(s)"} • ₹${"%.2f".format(sub.price)}", color = Color.White)
        }
    }
}
