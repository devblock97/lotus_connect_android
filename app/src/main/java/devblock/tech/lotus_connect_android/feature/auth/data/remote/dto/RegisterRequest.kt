package devblock.tech.lotus_connect_android.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
