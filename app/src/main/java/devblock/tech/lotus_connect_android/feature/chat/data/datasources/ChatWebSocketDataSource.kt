package devblock.tech.lotus_connect_android.feature.chat.data.datasources

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.MessageEntity
import devblock.tech.lotus_connect_android.feature.chat.domain.entities.PeerTypingEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

interface ChatWebSocketDataSource {
    fun connect(conversationId: String, token: String?): Flow<MessageEntity>
    fun observeTyping(conversationId: String): Flow<Boolean>
    fun sendTyping(recipientId: String, conversationId: String, isTyping: Boolean): Boolean
    fun sendMessage(content: String): Boolean
    fun disconnect()
}

class ChatWebSocketDataSourceImpl(
    private val client: OkHttpClient,
    private val gson: Gson = Gson()
) : ChatWebSocketDataSource {

    private var webSocket: WebSocket? = null
    private val tag = "ChatWebSocket"
    private val _typingEvents = MutableSharedFlow<PeerTypingEvent>(extraBufferCapacity = 64)

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

                        when (event) {
                            "chat:message" -> {
                                val payload = jsonObject.getAsJsonObject("payload") ?: return
                                val msgConvId = payload.get("conversation_id")?.asString
                                    ?: payload.get("conversationId")?.asString

                                if (msgConvId == null || msgConvId == conversationId) {
                                    val message = gson.fromJson(payload, MessageEntity::class.java)
                                    if (message != null && message.id.isNotBlank()) {
                                        trySend(message)
                                    }
                                }
                            }
                            "typing" -> {
                                val payload = jsonObject.getAsJsonObject("payload") ?: return
                                val senderId = payload.get("senderId")?.asString ?: ""
                                val msgConvId = payload.get("conversationId")?.asString ?: conversationId
                                val isTyping = payload.get("isTyping")?.asBoolean ?: false

                                _typingEvents.tryEmit(
                                    PeerTypingEvent(
                                        conversationId = msgConvId,
                                        senderId = senderId,
                                        isTyping = isTyping
                                    )
                                )
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

    override fun observeTyping(conversationId: String): Flow<Boolean> {
        return _typingEvents
            .filter { it.conversationId == conversationId }
            .map { it.isTyping }
    }

    override fun sendTyping(
        recipientId: String,
        conversationId: String,
        isTyping: Boolean
    ): Boolean {
        if (webSocket == null || recipientId.isBlank()) return false

        val envelop = mapOf(
            "event" to "typing",
            "payload" to mapOf(
                "recipientId" to recipientId,
                "conversationId" to conversationId,
                "isTyping" to isTyping
            )
        )

        val json = gson.toJson(envelop)
        return sendMessage(json)
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