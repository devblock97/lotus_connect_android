package devblock.tech.lotus_connect_android.feature.chat.presentation.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TypingIndicatorBubble(
    peerName: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BouncingDot(delayMillis = 0)
                BouncingDot(delayMillis = 150)
                BouncingDot(delayMillis = 300)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${peerName ?: "Peer"} is typing",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun BouncingDot(delayMillis: Int) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong().milliseconds)
        offsetY.animateTo(
            targetValue = -5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 350, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .size(6.dp)
            .graphicsLayer { translationY = offsetY.value }
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}