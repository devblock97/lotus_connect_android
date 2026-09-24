package devblock.tech.lotus_connect_android.feature.auth.data.datasources

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import devblock.tech.lotus_connect_android.feature.auth.domain.entities.User

class AuthLocalDataSource(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(accessToken: String, refreshToken: String, user: User) {
        println("check avatar url save: ${user.avatarUrl}")
        prefs.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .putString("user_id", user.id)
                .putString("user_email", user.email)
                .putString("user_fullname", user.fullName)
                .putString("avatar", user.avatarUrl)
        }
    }

    fun hasSession(): Boolean = !getAccessToken().isNullOrBlank()

    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun getCachedUser(): User? {
        val id = prefs.getString("user_id", null) ?: return null
        val username = prefs.getString("user_name", "") ?: ""
        val email = prefs.getString("user_email", "") ?: ""
        val fullName = prefs.getString("user_fullname", null)
        val avatar = prefs.getString("avatar", null)
        println("check avatar url get: $avatar")
        return User(
            id = id,
            username = username,
            email = email,
            fullName = fullName,
            avatarUrl = avatar
        )
    }

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun clearSession() {
        prefs.edit { clear() }
    }
}