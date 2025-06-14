package hr.unipu.java.soulmatch.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object AppData {
    val users = mutableListOf<User>()

    private val userFile = File("users.json")

    fun saveUsers() {
        try {
            val jsonString = Json.encodeToString(users)
            userFile.writeText(jsonString)
            println("Users successfully saved to ${userFile.absolutePath}")
        } catch (e: Exception) {
            println("Error saving users: ${e.message}")
        }
    }

    fun loadUsers() {
        try {
            if (userFile.exists()) {
                val jsonString = userFile.readText()
                if (jsonString.isNotBlank()) {
                    val loadedUsers = Json.decodeFromString<List<User>>(jsonString)
                    users.clear()
                    users.addAll(loadedUsers)
                    println("${users.size} users loaded from ${userFile.absolutePath}")
                }
            } else {
                println("User data file not found. Starting with a new list.")
            }
        } catch (e: Exception) {
            println("Error loading users: ${e.message}")
        }
    }
}