package hr.unipu.java.soulmatch.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val password: String,

    var name: String = "",
    var age: Int = 0,
    var bio: String = "",

    var city: String = "",
    var country: String = "",


    var gender: String = "",
    var seeking: List<String> = emptyList(),

    var profilePictureUrl: String = "",
    var imageUrls: List<String> = emptyList(),

    var interests: List<String> = emptyList(),


    val likes: MutableSet<String> = mutableSetOf(),
    val dislikes: MutableSet<String> = mutableSetOf(),
    val matches: MutableSet<String> = mutableSetOf()
)