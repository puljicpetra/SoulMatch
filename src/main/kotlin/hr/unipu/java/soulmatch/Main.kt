package hr.unipu.java.soulmatch

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.ui.screens.*
import java.io.File

enum class Screen {
    Welcome, Login, Signup, ProfileSetup, FindMatch, MyProfile
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
        println("[DEBUG Main.kt] Window recomposing.")
        var currentScreen by remember { mutableStateOf(Screen.Welcome) }

        var profilePreviewBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var profileFileToSave by remember { mutableStateOf<File?>(null) }
        var showFileChooser by remember { mutableStateOf(false) }

        println("[DEBUG Main.kt] Current State -> showFileChooser: $showFileChooser, profileFileToSave: ${profileFileToSave?.name}")

        val clearPreviewState = {
            println("[DEBUG Main.kt] clearPreviewState called.")
            profilePreviewBitmap = null
            profileFileToSave = null
        }

        val onNavigate: (Screen) -> Unit = { screen ->
            println("[DEBUG Main.kt] onNavigate called. Navigating to: $screen")
            currentScreen = screen
        }

        val onShowFileChooserChange = { shouldShow: Boolean ->
            println("[DEBUG Main.kt] onShowFileChooserChange called with: $shouldShow")
            showFileChooser = shouldShow
        }

        val onPreviewBitmapChange = { newBitmap: ImageBitmap? ->
            println("[DEBUG Main.kt] onPreviewBitmapChange called. New bitmap is ${if (newBitmap != null) "NOT NULL" else "NULL"}.")
            profilePreviewBitmap = newBitmap
        }

        val onFileToSaveChange = { newFile: File? ->
            println("[DEBUG Main.t] onFileToSaveChange called. New file is: ${newFile?.name}")
            profileFileToSave = newFile
        }


        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(onNavigate = onNavigate)
            Screen.Signup -> SignupScreen(onNavigate = onNavigate)
            Screen.Login -> LoginScreen(onNavigate = onNavigate)

            Screen.ProfileSetup -> ProfileSetupScreen(
                onNavigate = onNavigate,
                clearPreviewState = clearPreviewState,
                previewBitmap = profilePreviewBitmap,
                onPreviewBitmapChange = onPreviewBitmapChange,
                fileToSave = profileFileToSave,
                onFileToSaveChange = onFileToSaveChange,
                showFileChooser = showFileChooser,
                onShowFileChooserChange = onShowFileChooserChange
            )

            Screen.FindMatch -> FindMatchScreen(onNavigate = onNavigate)

            Screen.MyProfile -> MyProfileScreen(
                onNavigate = onNavigate,
                clearPreviewState = clearPreviewState,
                previewBitmap = profilePreviewBitmap,
                onPreviewBitmapChange = onPreviewBitmapChange,
                fileToSave = profileFileToSave,
                onFileToSaveChange = onFileToSaveChange,
                showFileChooser = showFileChooser,
                onShowFileChooserChange = onShowFileChooserChange
            )
        }
    }
}