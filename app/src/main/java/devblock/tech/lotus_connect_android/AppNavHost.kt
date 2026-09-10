package devblock.tech.lotus_connect_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import devblock.tech.lotus_connect_android.feature.auth.presentation.AuthScreen
import devblock.tech.lotus_connect_android.feature.auth.presentation.AuthViewModel
import devblock.tech.lotus_connect_android.feature.chat.presentation.ChatScreen
import devblock.tech.lotus_connect_android.feature.contacts.presentation.ContactsScreen
import devblock.tech.lotus_connect_android.feature.home.presentation.HomeScreen
import devblock.tech.lotus_connect_android.feature.notifications.presentation.NotificationsScreen
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model.NotificationViewModel
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
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
                val uiState by authViewModel.uiState.collectAsState()

                if (uiState.isCheckingSession) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    AuthScreen(
                        onAuthSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.AUTH) { inclusive = true }
                            }
                        },
                        viewModel = authViewModel
                    )
                }

            }
            composable(Routes.HOME) {
                HomeScreen()
            }
            composable(Routes.CHAT) {
                ChatScreen()
            }
            composable(Routes.NOTIFICATIONS) {
                val notificationViewModel: NotificationViewModel = viewModel(
                    factory = NotificationViewModel.Factory
                )
                val uiState by notificationViewModel.uiState.collectAsState()

                NotificationsScreen(
                    viewModel = notificationViewModel
                )
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
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}