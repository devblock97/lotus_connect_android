package devblock.tech.lotus_connect_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.data.local.AuthLocalDataSource
import devblock.tech.lotus_connect_android.feature.chat.presentation.ChatScreen
import devblock.tech.lotus_connect_android.feature.contacts.presentation.ContactsScreen
import devblock.tech.lotus_connect_android.feature.home.presentation.HomeScreen
import devblock.tech.lotus_connect_android.feature.settings.presentation.SettingsScreen
import devblock.tech.lotus_connect_android.ui.theme.LotusConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localDataSource = AuthLocalDataSource(applicationContext)
        RetrofitClient.initialize {
            localDataSource.getAccessToken()
        }
        enableEdgeToEdge()
        setContent {
            LotusConnectTheme {
                AppNavHost()
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomNavItem(Routes.HOME, "Home", Icons.Filled.Home)
    object ChatScreen: BottomNavItem(Routes.CHAT, "Chat", Icons.Filled.ChatBubbleOutline)
    object NotificationsScreen: BottomNavItem(Routes.NOTIFICATIONS, "Alerts", Icons.Filled.NotificationsNone)
    object ContactsScreen: BottomNavItem(Routes.CONTACTS, "Contacts", Icons.Filled.Person)
    object Settings: BottomNavItem(Routes.SETTINGS, "Settings", Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.ChatScreen,
    BottomNavItem.NotificationsScreen,
    BottomNavItem.ContactsScreen,
    BottomNavItem.Settings,
)

@Composable
fun AppBottomNavBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onItemSelected(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}