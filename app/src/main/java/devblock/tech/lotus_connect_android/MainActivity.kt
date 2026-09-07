package devblock.tech.lotus_connect_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import devblock.tech.lotus_connect_android.ui.theme.LotusConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LotusConnectTheme {
                MainScreen()
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object  Home: BottomNavItem("home", "Home", Icons.Filled.Home)
    object ChatScreen: BottomNavItem("chat", "Chat", Icons.Filled.ChatBubbleOutline)
    object ContactsScreen: BottomNavItem("contacts", "Contacts", Icons.Filled.Person)
    object Settings: BottomNavItem("settings", "Settings", Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.ChatScreen,
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

@Composable
fun MainScreen() {
    var selectedRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                selectedRoute = selectedRoute,
                onItemSelected = { selectedRoute = it }
            )
        }
    ) { innerPadding ->
        when(selectedRoute) {
            BottomNavItem.Home.route -> HomeScreen(Modifier.padding(innerPadding))
            BottomNavItem.ChatScreen.route -> ChatScreen(Modifier.padding(innerPadding))
            BottomNavItem.ContactsScreen.route -> ContactsScreen(Modifier.padding(innerPadding))
            BottomNavItem.Settings.route -> SettingsScreen(Modifier.padding(innerPadding))
        }
    }
}

@Composable fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Home screen",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable fun ChatScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat screen",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable fun ContactsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Contacts screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
@Composable fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LotusConnectTheme {
        MainScreen()
    }
}