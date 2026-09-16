package devblock.tech.lotus_connect_android.core.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

val gradientPalette = listOf(
    listOf(Color(0xFF667EEA), Color(0xFF764BA2)), // Indigo -> Purple
    listOf(Color(0xFFF093FB), Color(0xFFF5576C)), // Pink -> Red
    listOf(Color(0xFF4FACFE), Color(0xFF00F2FE)), // Blue -> Cyan
    listOf(Color(0xFF43E97B), Color(0xFF38F9D7)), // Green -> Teal
    listOf(Color(0xFFFA709A), Color(0xFFFEE140)), // Rose -> Yellow
    listOf(Color(0xFF30CFD0), Color(0xFF330867)), // Teal -> Deep purple
)

fun gradientFor(id: String): List<Color> {
    val index = abs(id.hashCode()) % gradientPalette.size
    return gradientPalette[index]
}