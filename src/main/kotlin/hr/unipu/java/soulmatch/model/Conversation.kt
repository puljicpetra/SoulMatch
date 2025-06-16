package hr.unipu.java.soulmatch.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val participantIds: Set<String>,
    val messageIds: MutableList<String> = mutableListOf(),
    var lastActivityTimestamp: Long = System.currentTimeMillis()
)