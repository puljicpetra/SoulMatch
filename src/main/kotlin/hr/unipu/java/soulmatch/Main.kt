package hr.unipu.java.soulmatch

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.ui.screens.*
import java.io.File

enum class Screen {
    Welcome, Login, Signup, ProfileSetup, FindMatch, MyProfile,
    Matches, Chat
}

fun main() = application {
    AppData.loadAllData()

    Window(
        onCloseRequest = {
            AppData.saveAllData()
            exitApplication()
        },
        title = "SoulMatch"
    ) {
        var currentScreen by remember { mutableStateOf(Screen.Welcome) }
        var currentChatId by remember { mutableStateOf<String?>(null) }

        var profilePreviewBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var profileFileToSave by remember { mutableStateOf<File?>(null) }
        var showFileChooser by remember { mutableStateOf(false) }

        val clearPreviewState = {
            profilePreviewBitmap = null
            profileFileToSave = null
        }

        val onNavigate: (Screen, String?) -> Unit = { screen, chatId ->
            println("[DEBUG Main.kt] Navigating to: $screen, with chatId: $chatId")
            currentScreen = screen
            currentChatId = chatId
        }

        val onShowFileChooserChange = { shouldShow: Boolean -> showFileChooser = shouldShow }
        val onPreviewBitmapChange = { newBitmap: ImageBitmap? -> profilePreviewBitmap = newBitmap }
        val onFileToSaveChange = { newFile: File? -> profileFileToSave = newFile }


        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen { screen -> onNavigate(screen, null) }
            Screen.Signup -> SignupScreen { screen -> onNavigate(screen, null) }
            Screen.Login -> LoginScreen { screen -> onNavigate(screen, null) }

            Screen.ProfileSetup -> ProfileSetupScreen(
                onNavigate = { screen -> onNavigate(screen, null) },
                clearPreviewState = clearPreviewState,
                previewBitmap = profilePreviewBitmap,
                onPreviewBitmapChange = onPreviewBitmapChange,
                fileToSave = profileFileToSave,
                onFileToSaveChange = onFileToSaveChange,
                showFileChooser = showFileChooser,
                onShowFileChooserChange = onShowFileChooserChange
            )

            Screen.FindMatch -> FindMatchScreen { screen -> onNavigate(screen, null) }

            Screen.MyProfile -> MyProfileScreen(
                onNavigate = { screen -> onNavigate(screen, null) },
                clearPreviewState = clearPreviewState,
                previewBitmap = profilePreviewBitmap,
                onPreviewBitmapChange = onPreviewBitmapChange,
                fileToSave = profileFileToSave,
                onFileToSaveChange = onFileToSaveChange,
                showFileChooser = showFileChooser,
                onShowFileChooserChange = onShowFileChooserChange
            )

            Screen.Matches -> MatchesScreen(onNavigate = onNavigate)

            Screen.Chat -> {
                val chatId = currentChatId
                if (chatId != null) {
                    ChatScreen(
                        conversationId = chatId,
                        onNavigateBack = { onNavigate(Screen.Matches, null) }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        println("[ERROR Main.kt] Chat screen opened without a chatId. Redirecting to Matches.")
                        onNavigate(Screen.Matches, null)
                    }
                }
            }
        }
    }
}