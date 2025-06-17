package hr.unipu.java.soulmatch.model

import hr.unipu.java.soulmatch.getAppDataDirectory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object AppData {
    val users = mutableListOf<User>()
    val conversations = mutableListOf<Conversation>()
    val messages = mutableListOf<Message>()

    var currentUser: User? = null

    private val userFile = File(getAppDataDirectory(), "users.json")
    private val conversationFile = File(getAppDataDirectory(), "conversations.json")
    private val messageFile = File(getAppDataDirectory(), "messages.json")


    fun userLikes(liker: User, liked: User): Boolean {
        liker.likes.add(liked.id)
        println("${liker.name} liked ${liked.name}")

        if (liked.likes.contains(liker.id)) {
            liker.matches.add(liked.id)
            liked.matches.add(liker.id)
            println("IT'S A MATCH BETWEEN ${liker.name} and ${liked.name}!")
            saveAllData()
            return true
        }

        saveAllData()
        return false
    }

    fun userDislikes(disliker: User, disliked: User) {
        disliker.dislikes.add(disliked.id)
        println("${disliker.name} disliked ${disliked.name}")
        saveAllData()
    }

    fun saveAllData() {
        saveUsers()
        saveConversations()
        saveMessages()
        println("All app data saved.")
    }

    fun loadAllData() {
        loadUsers()
        loadConversations()
        loadMessages()
        println("All app data loaded.")
    }


    fun saveUsers() {
        try {
            val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
            val jsonString = json.encodeToString(users)
            userFile.writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving users: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadUsers() {
        try {
            if (userFile.exists()) {
                val jsonString = userFile.readText()
                if (jsonString.isNotBlank()) {
                    val json = Json { ignoreUnknownKeys = true }
                    val loadedUsers = json.decodeFromString<List<User>>(jsonString)
                    users.clear()
                    users.addAll(loadedUsers)
                }
            }
        } catch (e: Exception) {
            println("Error loading users from ${userFile.absolutePath}: ${e.message}")
            e.printStackTrace()
        }
    }

    fun saveConversations() {
        try {
            val json = Json { prettyPrint = true }
            val jsonString = json.encodeToString(conversations)
            conversationFile.writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving conversations: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadConversations() {
        try {
            if (conversationFile.exists()) {
                val jsonString = conversationFile.readText()
                if (jsonString.isNotBlank()) {
                    val json = Json { ignoreUnknownKeys = true }
                    val loaded = json.decodeFromString<List<Conversation>>(jsonString)
                    conversations.clear()
                    conversations.addAll(loaded)
                }
            }
        } catch (e: Exception) {
            println("Error loading conversations: ${e.message}")
            e.printStackTrace()
        }
    }

    fun saveMessages() {
        try {
            val json = Json { prettyPrint = true }
            val jsonString = json.encodeToString(messages)
            messageFile.writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving messages: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadMessages() {
        try {
            if (messageFile.exists()) {
                val jsonString = messageFile.readText()
                if (jsonString.isNotBlank()) {
                    val json = Json { ignoreUnknownKeys = true }
                    val loaded = json.decodeFromString<List<Message>>(jsonString)
                    messages.clear()
                    messages.addAll(loaded)
                }
            }
        } catch (e: Exception) {
            println("Error loading messages: ${e.message}")
            e.printStackTrace()
        }
    }
}