package devblock.tech.lotus_connect_android

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import devblock.tech.lotus_connect_android.feature.auth.presentation.AuthScreen
import devblock.tech.lotus_connect_android.feature.chat.presentation.ChatScreen
import devblock.tech.lotus_connect_android.feature.contacts.presentation.ContactsScreen
import devblock.tech.lotus_connect_android.feature.home.presentation.HomeScreen
import devblock.tech.lotus_connect_android.feature.settings.presentation.SettingsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Routes.AUTH) {
                AppBottomNavBar(
                    selectedRoute = currentRoute ?: Routes.HOME,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.AUTH,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Routes.AUTH) {
                AuthScreen(
                    onAuthSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.AUTH) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.HOME) {
                HomeScreen()
            }
            composable(Routes.CHAT) {
                ChatScreen()
            }
            composable(Routes.CONTACTS) {
                ContactsScreen()
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    isLoggedIn = true,
                    onLoginClick = { navController.navigate(Routes.AUTH) },
                    onLogout = {
                        navController.navigate(Routes.AUTH) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}