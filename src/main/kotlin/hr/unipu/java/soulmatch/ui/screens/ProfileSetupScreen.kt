package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.loadImageBitmap
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.ui.composables.FileDialog
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
    var city by remember { mutableStateOf(currentUser.city) }
    var country by remember { mutableStateOf(currentUser.country) }
    var interests by remember { mutableStateOf(currentUser.interests.joinToString(", ")) }

    var selectedImageName by remember { mutableStateOf(currentUser.profilePictureUrl) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var showFileChooser by remember { mutableStateOf(false) }

    LaunchedEffect(selectedImageName) {
        if (selectedImageName.isNotBlank()) {
            imageBitmap = loadImageBitmap(File("src/main/resources/images/$selectedImageName"))
        } else {
            imageBitmap = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set Up Your Profile", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Let others get to know you!", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        if (imageBitmap != null) {
            Image(bitmap = imageBitmap!!, contentDescription = "Profile Picture", modifier = Modifier.size(120.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        } else {
            Box(modifier = Modifier.size(120.dp).background(Color.LightGray, CircleShape)) {
                Icon(Icons.Default.Person, "Placeholder", modifier = Modifier.fillMaxSize(0.6f).align(Alignment.Center), tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showFileChooser = true }) {
            Text(if (selectedImageName.isBlank()) "Add Profile Picture" else "Change Profile Picture")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { if (it.all(Char::isDigit)) age = it }, label = { Text("Your Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = interests, onValueChange = { interests = it }, label = { Text("Your Interests (comma-separated)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("About yourself...") }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                currentUser.name = name
                currentUser.age = age.toIntOrNull() ?: 0
                currentUser.bio = bio
                currentUser.profilePictureUrl = selectedImageName
                currentUser.city = city
                currentUser.country = country
                currentUser.interests = interests.split(',').map { it.trim() }.filter { it.isNotBlank() }

                AppData.saveUsers()
                println("Profile updated for user: ${currentUser.email}")
                onNavigate(Screen.FindMatch)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
        ) {
            Text("Begin your search for a match", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showFileChooser) {
        FileDialog(
            onCloseRequest = { selectedFile: File? ->
                showFileChooser = false
                if (selectedFile != null) {
                    val imageDir = File("src/main/resources/images")
                    if (!imageDir.exists()) imageDir.mkdirs()
                    val destinationFile = File(imageDir, selectedFile.name)
                    selectedFile.copyTo(destinationFile, overwrite = true)
                    selectedImageName = destinationFile.name
                }
            }
        )
    }
}