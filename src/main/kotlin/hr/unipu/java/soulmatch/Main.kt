package hr.unipu.java.soulmatch

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hr.unipu.java.soulmatch.ui.screens.LoginScreen
import hr.unipu.java.soulmatch.ui.screens.SignupScreen
import hr.unipu.java.soulmatch.ui.screens.WelcomeScreen

enum class Screen {
    Welcome,
    Login,
    Signup,
    FindMatch
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "SoulMatch") {
        var currentScreen by remember { mutableStateOf(Screen.Welcome) }

        val onNavigate: (Screen) -> Unit = { screen ->
            currentScreen = screen
        }

        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(onNavigate = onNavigate)
            Screen.Signup -> SignupScreen(onNavigate = onNavigate)
            Screen.Login -> LoginScreen(onNavigate = onNavigate)


            else -> WelcomeScreen(onNavigate = onNavigate)
        }
    }
}