package devblock.tech.lotus_connect_android.feature.settings.presentation.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import devblock.tech.lotus_connect_android.core.utils.copyUriToFile
import devblock.tech.lotus_connect_android.feature.settings.presentation.view_model.ProfileViewModel

@Composable
fun UploadAvatarScreen(
    viewModel: ProfileViewModel,
    avatarUrl: String,
    onDone: () -> Unit = {}
) {
    val context = LocalContext.current
    val uploadState by viewModel.uiState.collectAsState()

    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            pickedImageUri = uri
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Update Profile Photo",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a clear photo so people can recognize you",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val displayModel: Any? = pickedImageUri ?: avatarUrl.takeIf { it.isNotBlank() }
                ?.replace("127.0.0.1", "10.0.2.2")

            if (displayModel != null) {
                AsyncImage(
                    model = displayModel,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "No Avatar",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (uploadState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clickable(enabled = !uploadState.isLoading) {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Change photo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (uploadState.isSuccess) {
            Text(
                text = "Photo updated...",
                color = Color(0xFF34A853),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        } else if (uploadState.errorMessage != null) {
            Text(
                text = uploadState.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                pickedImageUri?.let { uri ->
                    val file = copyUriToFile(context, uri)
                    viewModel.uploadAvatar(file)
                }
            },
            enabled = pickedImageUri != null && !uploadState.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (uploadState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Photo")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
