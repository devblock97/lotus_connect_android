package devblock.tech.lotus_connect_android.core.theme

import android.os.Messenger
import androidx.compose.ui.graphics.Color

object AppColor {
    // --- Messenger / Chat Screen Colors ---
    val MessengerBlue = Color(0xFF0084FF)
    val MessengerBackground = Color(0xFF000000)
    val MessengerReceiveBubble = Color(0xFF262628)
    val MessengerInputPill = Color(0xFF242526)
    val MessengerMutedText = Color(0xFF8E8E93)

    // --- Sematic Chat Aliases ---
    val ChatPrimary = MessengerBlue
    val ChatBackground = MessengerBackground
    val ChatReceivedBubble = MessengerReceiveBubble
    val ChatInputPill = MessengerInputPill
    val ChatMuted = MessengerMutedText
    val ChatCheckmark = MessengerBlue
    val ChatWhite = Color(0xFFFFFFFF)

    // --- Common Brand & Status Colors ---
    val BrandBlue = Color(0xFF0095F6)
    val SuccessGreen = Color(0xFF34A853)
    val ErrorRed = Color(0xFFEA3445)
    val WarningYellow = Color(0xFFFBBC05)
    val TextMuted = Color(0xFF9AA0A6)
    val DarkCard = Color(0xFF1E1E1E)
    val DarkSurface = Color(0xFF121212)
    val DarkDivider = Color(0xFF2E2E2E)
}