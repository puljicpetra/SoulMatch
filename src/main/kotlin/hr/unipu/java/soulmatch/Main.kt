package hr.unipu.java.soulmatch

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.ui.screens.*

enum class Screen {
    Welcome,
    Login,
    Signup,
    ProfileSetup,
    FindMatch,
    MyProfile
}

fun main() = application {
    AppData.loadUsers()

    Window(
        onCloseRequest = {
            AppData.saveUsers()
            exitApplication()
        },
        title = "SoulMatch"
    ) {
        var currentScreen by remember { mutableStateOf(Screen.Welcome) }

        val onNavigate: (Screen) -> Unit = { screen ->
            currentScreen = screen
        }

        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(onNavigate = onNavigate)
            Screen.Signup -> SignupScreen(onNavigate = onNavigate)
            Screen.Login -> LoginScreen(onNavigate = onNavigate)
            Screen.ProfileSetup -> ProfileSetupScreen(onNavigate = onNavigate)
            Screen.FindMatch -> FindMatchScreen(onNavigate = onNavigate)
            Screen.MyProfile -> MyProfileScreen(onNavigate = onNavigate)

            else -> WelcomeScreen(onNavigate = onNavigate)
        }
    }
}