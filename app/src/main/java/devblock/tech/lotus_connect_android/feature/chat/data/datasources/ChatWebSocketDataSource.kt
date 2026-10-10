package devblock.tech.lotus_connect_android.feature.chat.data.datasources

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

interface ChatWebSocketDataSource {
    fun connect(conversationId: String, token: String?): Flow<MessageEntity>
    fun sendMessage(content: String): Boolean
    fun disconnect()
}

class ChatWebSocketDataSourceImpl(
    private val client: OkHttpClient,
    private val gson: Gson = Gson()
) : ChatWebSocketDataSource {

    private var webSocket: WebSocket? = null
    private val tag = "ChatWebSocket"

    override fun connect(
        conversationId: String,
        token: String?
    ): Flow<MessageEntity> = callbackFlow {
        val wsClient = client.newBuilder()
            .pingInterval(30, TimeUnit.SECONDS)
            .build()

        val wsUrl = if (!token.isNullOrBlank()) {
            "ws://10.0.2.2:8080/api/v1/ws?token=$token"
        } else {
            "ws://10.0.2.2:8080/api/v1/ws"
        }

        Log.d(tag, "Connecting to WebSocket: $wsUrl (conversationId=$conversationId)")

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(tag, "WebSocket connection established!")

                // Notify backend that client is actively viewing this conversation
                val focusMessage = gson.toJson(
                    mapOf(
                        "event" to "chat:focus",
                        "payload" to mapOf("conversationId" to conversationId)
                    )
                )
                webSocket.send(focusMessage)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    Log.d(tag, "Received WS raw message: $text")
                    val jsonElement = JsonParser.parseString(text)
                    if (jsonElement.isJsonObject) {
                        val jsonObject = jsonElement.asJsonObject
                        val event = jsonObject.get("event")?.asString

                        // Handle chat:message event from gateway
                        if (event == "chat:message") {
                            val payload = jsonObject.get("payload")
                            if (payload != null && payload.isJsonObject) {
                                val payloadObj = payload.asJsonObject
                                val msgConvId = payloadObj.get("conversation_id")?.asString
                                    ?: payloadObj.get("conversationId")?.asString

                                // Only emit if it matches current conversation or no conversationId restriction
                                if (msgConvId == null || msgConvId == conversationId) {
                                    val message = gson.fromJson(payloadObj, MessageEntity::class.java)
                                    if (message != null && message.id.isNotBlank()) {
                                        Log.d(tag, "Emitting received message: ${message.id} -> ${message.content}")
                                        trySend(message)
                                    }
                                }
                            }
                        } else if (jsonObject.has("id") && jsonObject.has("content")) {
                            // Direct MessageEntity format fallback
                            val message = gson.fromJson(jsonObject, MessageEntity::class.java)
                            if (message != null && message.id.isNotBlank()) {
                                trySend(message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(tag, "Failed to parse incoming WebSocket message: $text", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(tag, "WebSocket error: ${t.message}, responseCode=${response?.code}", t)
                close(t)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(tag, "WebSocket closed (code=$code, reason=$reason)")
                channel.close()
            }
        }

        webSocket = wsClient.newWebSocket(request, listener)

        awaitClose {
            Log.d(tag, "Closing WebSocket for conversation $conversationId")
            val unfocusMessage = gson.toJson(
                mapOf(
                    "event" to "chat:focus",
                    "payload" to mapOf<String, Any?>("conversationId" to null)
                )
            )
            webSocket?.send(unfocusMessage)
            webSocket?.close(1000, "Screen closed")
            webSocket = null
        }
    }

    override fun sendMessage(content: String): Boolean {
        val ws = webSocket
        if (ws == null) {
            Log.w(tag, "Cannot send message: WebSocket is not connected")
            return false
        }

        Log.d(tag, "Sending message over WebSocket: $content")
        return ws.send(content)
    }

    override fun disconnect() {
        webSocket?.close(1000, "User left")
        webSocket = null
    }
}