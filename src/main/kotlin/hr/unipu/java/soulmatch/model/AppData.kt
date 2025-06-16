package hr.unipu.java.soulmatch.model

import hr.unipu.java.soulmatch.getAppDataDirectory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object AppData {
    val users = mutableListOf<User>()
    var currentUser: User? = null

    private val userFile = File(getAppDataDirectory(), "users.json")



    fun userLikes(liker: User, liked: User): Boolean {
        liker.likes.add(liked.id)
        println("${liker.name} liked ${liked.name}")

        if (liked.likes.contains(liker.id)) {
            liker.matches.add(liked.id)
            liked.matches.add(liker.id)
            println("IT'S A MATCH BETWEEN ${liker.name} and ${liked.name}!")
            saveUsers()
            return true
        }

        saveUsers()
        return false
    }

    /**
     * Records that one user has disliked another.
     * @param disliker The user who performed the dislike action.
     * @param disliked The user who was disliked.
     */
    fun userDislikes(disliker: User, disliked: User) {
        disliker.dislikes.add(disliked.id)
        println("${disliker.name} disliked ${disliked.name}")
        saveUsers()
    }


    fun saveUsers() {
        try {
            val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
            val jsonString = json.encodeToString<List<User>>(users)
            userFile.writeText(jsonString)
            println("Users successfully saved to ${userFile.absolutePath}")
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
                    println("${users.size} users loaded from ${userFile.absolutePath}")
                }
            } else {
                println("User data file not found at ${userFile.absolutePath}. Starting with a new list.")
            }
        } catch (e: Exception) {
            println("Error loading users from ${userFile.absolutePath}: ${e.message}")
            e.printStackTrace()
            }
        }
}