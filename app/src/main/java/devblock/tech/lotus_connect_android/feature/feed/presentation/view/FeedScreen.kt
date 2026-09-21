package devblock.tech.lotus_connect_android.feature.feed.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun FeedScreen(modifier: Modifier = Modifier) {
    AsyncImage(
        model = "http://10.0.2.2:8080/api/v1//uploads/01a04692-e975-7131-a1de-75cd987dffb3-19.png",
        contentDescription = "Image"
    )
}