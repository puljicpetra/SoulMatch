package hr.unipu.java.soulmatch.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val password: String
)