package devblock.tech.lotus_connect_android.feature.contacts.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import devblock.tech.lotus_connect_android.feature.contacts.presentation.view_model.ContactsViewModel
import devblock.tech.lotus_connect_android.feature.contacts.presentation.widgets.ContactCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contacts",
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        }
    ) { innerPadding ->
        if (uiState.friends.isEmpty()) {
            Text("Empty")
        }
        if (uiState.friends.isNotEmpty() && !uiState.isLoading) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(uiState.friends, key = { it.id }) { friend ->
                    ContactCard(friend = friend)
                }
            }
        }
    }
}