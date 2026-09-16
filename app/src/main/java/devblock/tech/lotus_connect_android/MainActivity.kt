package devblock.tech.lotus_connect_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import devblock.tech.lotus_connect_android.core.network.RetrofitClient
import devblock.tech.lotus_connect_android.feature.auth.data.local.AuthLocalDataSource
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
    object ChatScreen: BottomNavItem(Routes.CONVERSATIONS, "Chat", Icons.Filled.ChatBubbleOutline)
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