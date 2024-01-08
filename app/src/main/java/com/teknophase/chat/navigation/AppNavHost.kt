package com.teknophase.chat.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.teknophase.chat.ui.auth.LoginScreen
import com.teknophase.chat.ui.auth.RegistrationScreen
import com.teknophase.chat.ui.screens.ChatListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            if (navController.currentDestination?.route?.contains(AppNavRoutes.BOTTOM_NAVIGATION.route) == true)
                NavigationBar(modifier = Modifier.height(64.dp)) {
                    BottomNavRoutes.values().forEach { screen ->
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
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppNavRoutes.LOGIN.route,
            Modifier.padding(innerPadding)
        ) {
            navigation(
                route = AppNavRoutes.BOTTOM_NAVIGATION.route,
                startDestination = BottomNavRoutes.CHAT.route
            ) {
                composable(BottomNavRoutes.CHAT.route) { ChatListScreen(navController = navController) }
                composable(BottomNavRoutes.CALLS.route) { ChatListScreen(navController = navController) }
                composable(BottomNavRoutes.PROFILE.route) { ChatListScreen(navController = navController) }
            }

            composable(AppNavRoutes.LOGIN.route) {
                LoginScreen(onNavigate = {
                    navController.navigate(
                        it
                    )
                })
            }

            composable(AppNavRoutes.REGISTER.route) {
                RegistrationScreen(
                    onNavigate = {},
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

