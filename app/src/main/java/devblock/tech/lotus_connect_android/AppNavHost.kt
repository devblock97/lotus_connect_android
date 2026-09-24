package devblock.tech.lotus_connect_android

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.navArgument
import devblock.tech.lotus_connect_android.feature.auth.presentation.view.AuthScreen
import devblock.tech.lotus_connect_android.feature.auth.presentation.view_model.AuthViewModel
import devblock.tech.lotus_connect_android.feature.chat.presentation.view.ChatScreen
import devblock.tech.lotus_connect_android.feature.chat.presentation.view.ConversationListScreen
import devblock.tech.lotus_connect_android.feature.chat.presentation.view_model.ChatViewModel
import devblock.tech.lotus_connect_android.feature.chat.presentation.view_model.ConversationListViewModel
import devblock.tech.lotus_connect_android.feature.contacts.presentation.ContactsScreen
import devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model.ContactsViewModel
import devblock.tech.lotus_connect_android.feature.home.presentation.HomeScreen
import devblock.tech.lotus_connect_android.feature.home.presentation.view_model.FeedViewModel
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view.NotificationsScreen
import devblock.tech.lotus_connect_android.feature.notifications.presentation.view_model.NotificationViewModel
import devblock.tech.lotus_connect_android.feature.settings.presentation.view.SettingsScreen
import devblock.tech.lotus_connect_android.feature.settings.presentation.view.UploadAvatarScreen
import devblock.tech.lotus_connect_android.feature.settings.presentation.view_model.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
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
            modifier = Modifier.padding(innerPadding)
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
                val viewModel: FeedViewModel = viewModel(
                    factory = FeedViewModel.Factory
                )
                HomeScreen(
                    viewModel = viewModel
                )
            }
            composable(Routes.CONVERSATIONS) {
                val viewModel: ConversationListViewModel = viewModel(
                    factory = ConversationListViewModel.Factory
                )
                ConversationListScreen(
                    viewModel = viewModel,
                    onConversationClick = { conversationId ->
                        navController.navigate("chat/$conversationId")
                    }
                )
            }
            composable(
                route = Routes.CHAT,
                arguments = listOf(
                    navArgument("conversationId") { type = androidx.navigation.NavType.StringType }
                )
            ) { backStackEntry ->
                val conversationId = backStackEntry.arguments?.getString("conversationId").orEmpty()
                val viewModel: ChatViewModel = viewModel(
                    factory = ChatViewModel.Factory
                )
                ChatScreen(conversationId = conversationId, viewModel = viewModel)
            }
            composable(Routes.NOTIFICATIONS) {
                val notificationViewModel: NotificationViewModel = viewModel(
                    factory = NotificationViewModel.Factory
                )
                NotificationsScreen(
                    viewModel = notificationViewModel
                )
            }
            composable(Routes.CONTACTS) {
                val contactsViewModel: ContactsViewModel = viewModel(
                    factory = ContactsViewModel.Factory
                )
                ContactsScreen(
                    viewModel = contactsViewModel
                )
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
                    },
                    onEditAvatarClick = { avatarUrl ->
                        val encodedUrl = Uri.encode(avatarUrl)
                        navController.navigate("upload_avatar?avatarUrl=$encodedUrl")
                    }
                )
            }
            composable(
                Routes.UPLOAD_AVATAR,
                arguments = listOf(
                    navArgument("avatarUrl") {
                        type = androidx.navigation.NavType.StringType
                        nullable = true
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val rawAvatarUrl = backStackEntry.arguments?.getString("avatarUrl").orEmpty()
                val avatarUrl = Uri.decode(rawAvatarUrl)
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.Factory
                )
                UploadAvatarScreen(
                    viewModel = profileViewModel,
                    avatarUrl = avatarUrl,
                    onDone = { navController.popBackStack() }
                )
            }
        }
    }
}