package com.teknophase.chat.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.teknophase.chat.ui.screens.ChatListScreen
import com.teknophase.chat.ui.theme.AccentLight
import com.teknophase.chat.ui.theme.PrimaryBlue

// TODO: To be implemented
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.height(64.dp)) {
                AppNavRoutes.values().forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = screen.icon),
                                contentDescription = null
                            )
                        },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            unselectedIconColor = AccentLight
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppNavRoutes.CHAT.route,
            Modifier.padding(innerPadding)
        ) {
            composable(AppNavRoutes.CHAT.route) { ChatListScreen(navController = navController) }
            composable(AppNavRoutes.CALLS.route) { ChatListScreen(navController = navController) }
            composable(AppNavRoutes.PROFILE.route) { ChatListScreen(navController = navController) }
        }
    }
}

