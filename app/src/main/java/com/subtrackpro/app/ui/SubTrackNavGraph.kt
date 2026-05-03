package com.subtrackpro.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.subtrackpro.app.ui.screens.*

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Splash : Screen("splash", "", Icons.Default.Home)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Calendar : Screen("calendar", "Calendar", Icons.Default.CalendarMonth)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.PieChart)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

private val bottomScreens = listOf(Screen.Home, Screen.Calendar, Screen.Analytics, Screen.Settings)

@Composable
fun SubTrackNavGraph() {
    val nav = rememberNavController()
    val backstack by nav.currentBackStackEntryAsState()
    val route = backstack?.destination?.route
    val showBottom = bottomScreens.any { it.route == route }

    Scaffold(bottomBar = {
        if (showBottom) NavigationBar {
            bottomScreens.forEach { s ->
                NavigationBarItem(
                    selected = backstack?.destination?.hierarchy?.any { it.route == s.route } == true,
                    onClick = {
                        nav.navigate(s.route) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                    icon = { Icon(s.icon, s.label) },
                    label = { Text(s.label) }
                )
            }
        }
    }) { pad ->
        NavHost(nav, startDestination = Screen.Splash.route, modifier = Modifier.padding(pad)) {
            composable(Screen.Splash.route) {
                SplashScreen { nav.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route){inclusive=true} } }
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onAdd = { nav.navigate("addedit?id=") },
                    onItemClick = { nav.navigate("details/$it") }
                )
            }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable("addedit?id={id}") {
                val id = it.arguments?.getString("id")?.takeIf { s -> s.isNotBlank() }
                AddEditScreen(id) { nav.popBackStack() }
            }
            composable("details/{id}") {
                DetailsScreen(
                    id = it.arguments?.getString("id") ?: "",
                    onEdit = { id -> nav.navigate("addedit?id=$id") },
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
