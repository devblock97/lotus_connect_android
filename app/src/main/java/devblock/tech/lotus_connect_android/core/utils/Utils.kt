package devblock.tech.lotus_connect_android.core.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

@RequiresApi(Build.VERSION_CODES.O)
fun toDisplayDate(input: String?): String {
    if (input.isNullOrBlank()) return ""
    return try {
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        if (input.contains("T")) {
            // ISO-8601 instant/offset format: e.g. "2026-08-24T09:49:24.194704Z"
            try {
                Instant.parse(input).atZone(ZoneId.systemDefault()).format(outputFormatter)
            } catch (_: Exception) {
                LocalDateTime.parse(input).format(outputFormatter)
            }
        } else {
            // Date-only input: "2024-06-15"
            val date = LocalDate.parse(input)
            date.format(outputFormatter)
        }
    } catch (e: Exception) {
        // Fallback: safely extract YYYY-MM-DD or return raw input if parsing fails
        if (input.length >= 10 && input[4] == '-' && input[7] == '-') {
            val parts = input.take(10).split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            input
        }
    }
}

fun copyUriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "file_${System.currentTimeMillis()}.jpg")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}