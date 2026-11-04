package devblock.tech.lotus_connect_android.feature.chat.presentation.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devblock.tech.lotus_connect_android.core.theme.AppColor

@Composable
fun ChatTimestampHeader(timestamp: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = timestamp,
            color = AppColor.MessengerMutedText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}