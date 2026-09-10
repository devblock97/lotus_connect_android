package devblock.tech.lotus_connect_android.feature.settings.presentation

import android.graphics.drawable.Icon
import android.widget.Switch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import devblock.tech.lotus_connect_android.feature.auth.presentation.AuthViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = true,
    onLoginClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val currentUser = uiState.user

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (isLoggedIn) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = currentUser?.fullName?.takeIf { it.isNotBlank() }
                            ?: currentUser?.username
                            ?: "Guest user",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = currentUser?.username ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        SettingsSectionTitle("Account")
        SettingsItem(
            icon = Icons.Filled.Person,
            title = "Edit Profile",
            onClick = { }
        )
        SettingsItem(
            icon = Icons.Filled.Lock,
            title = "Change Password",
            onClick = { }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SettingsSectionTitle("Preferences")
        SettingSwitchItem(
            icon = Icons.Filled.Notifications,
            title = "Notifications",
            checked = true,
            onCheckedChange = { }
        )
        SettingSwitchItem(
            icon = Icons.Filled.DarkMode,
            title = "Dark Mode",
            checked = false,
            onCheckedChange = { }
        )
        SettingsItem(
            icon = Icons.Filled.Language,
            title = "Language",
            trailingText = "English",
            onClick = { }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SettingsSectionTitle("Support")
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            title = "Help Center",
            onClick = { }
        )
        SettingsItem(
            icon = Icons.Filled.Info,
            title = "About",
            trailingText = "v1.0.0",
            onClick = { }
        )
        SettingsItem(
            icon = Icons.Filled.PrivacyTip,
            title = "Privacy Policy",
            onClick =  { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (isLoggedIn) {
                Button(
                    onClick = { showLogoutDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            } else {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "Login")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login")
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out?") },
            text = { Text("Are you sure you want to log out of your account?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                    viewModel.logout()
                }) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    androidx.compose.material3.ListItem(
        headlineContent = { Text(title)},
        leadingContent = { Icon(icon, contentDescription = title)},
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                trailingText?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title)},
        leadingContent = { Icon(icon, contentDescription = title)},
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}
