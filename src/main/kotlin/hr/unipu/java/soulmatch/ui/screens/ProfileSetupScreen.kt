package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.AwtWindow
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData
import org.jetbrains.skia.Image as SkiaImage
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun ProfileSetupScreen(onNavigate: (Screen) -> Unit) {
    val currentUser = AppData.currentUser
    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: No user is logged in. Please restart the app.")
        }
        return
    }

    var name by remember { mutableStateOf(currentUser.name) }
    var age by remember { mutableStateOf(if (currentUser.age == 0) "" else currentUser.age.toString()) }
    var bio by remember { mutableStateOf(currentUser.bio) }
    var selectedImageName by remember { mutableStateOf(currentUser.profilePictureUrl) }

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(selectedImageName) {
        if (selectedImageName.isNotBlank()) {
            imageBitmap = loadImageBitmap(File("src/main/resources/images/$selectedImageName"))
        }
    }

    var showFileChooser by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set Up Your Profile", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Let others get to know you!", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(120.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(modifier = Modifier.size(120.dp).background(Color.LightGray, CircleShape)) {
                Icon(Icons.Default.Person, "Placeholder", modifier = Modifier.fillMaxSize(0.6f).align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showFileChooser = true }) {
            Text(if (selectedImageName.isBlank()) "Add Profile Picture" else "Change Profile Picture")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = age, onValueChange = { if (it.all(Char::isDigit)) age = it }, label = { Text("Your Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("About yourself...") }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                currentUser.name = name
                currentUser.age = age.toIntOrNull() ?: 0
                currentUser.bio = bio
                currentUser.profilePictureUrl = selectedImageName

                AppData.saveUsers()
                println("Profile updated for user: ${currentUser.email}")
                onNavigate(Screen.FindMatch)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
        ) {
            Text("Begin your search for a match", color = Color.White, fontSize = 16.sp)
        }
    }

    if (showFileChooser) {
        FileDialog(
            onCloseRequest = { selectedFile ->
                showFileChooser = false
                if (selectedFile != null) {
                    val imageDir = File("src/main/resources/images")
                    if (!imageDir.exists()) imageDir.mkdirs()

                    val destinationFile = File(imageDir, selectedFile.name)
                    selectedFile.copyTo(destinationFile, overwrite = true)

                    selectedImageName = destinationFile.name
                    imageBitmap = loadImageBitmap(destinationFile)
                    println("Image selected and copied: ${destinationFile.name}")
                }
            }
        )
    }
}

private fun loadImageBitmap(file: File): ImageBitmap? {
    return try {
        SkiaImage.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
    } catch (e: Exception) {
        println("Error loading image: ${e.message}")
        null
    }
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose an image", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(files.firstOrNull())
                }
            }
        }
    },
    dispose = FileDialog::dispose
)