package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
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
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData
import org.jetbrains.skia.Image as SkiaImage
import java.io.File

@Composable
fun FindMatchScreen(onNavigate: (Screen) -> Unit) {
    val potentialMatches = AppData.users.filter { it.id != AppData.currentUser?.id }
    var currentIndex by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SoulMatch") },
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White,
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile Options")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                menuExpanded = false
                                onNavigate(Screen.MyProfile)
                            }) {
                                Text("My Profile")
                            }
                            DropdownMenuItem(onClick = {
                                menuExpanded = false
                                AppData.currentUser = null
                                onNavigate(Screen.Welcome)
                            }) {
                                Text("Log Out")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (potentialMatches.isNotEmpty() && currentIndex < potentialMatches.size) {
                val userToShow = potentialMatches[currentIndex]

                Card(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (userToShow.profilePictureUrl.isNotBlank()) {
                            val bitmap = remember(userToShow.profilePictureUrl) {
                                loadImageBitmap(File("src/main/resources/images/${userToShow.profilePictureUrl}"))
                            }
                            if (bitmap != null) {
                                Image(bitmap = bitmap, contentDescription = "${userToShow.name}'s profile picture", modifier = Modifier.size(150.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                            } else {
                                ProfileImagePlaceholder()
                            }
                        } else {
                            ProfileImagePlaceholder()
                        }
                        Text(text = "${userToShow.name}, ${userToShow.age}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(text = userToShow.bio, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            println("Declined user: ${userToShow.name}")
                            currentIndex++
                        },
                        modifier = Modifier.size(72.dp), shape = CircleShape, border = BorderStroke(2.dp, Color.Red)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Decline", tint = Color.Red, modifier = Modifier.size(36.dp))
                    }
                    Button(
                        onClick = {
                            println("Liked user: ${userToShow.name}")
                            // TODO: Implement like logic
                            currentIndex++
                        },
                        modifier = Modifier.size(72.dp), shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Like", tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No more users to show. Come back later!", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileImagePlaceholder() {
    Box(modifier = Modifier.size(150.dp).background(Color.LightGray, CircleShape)) {
        Icon(
            Icons.Default.Person,
            contentDescription = "No profile picture",
            modifier = Modifier.fillMaxSize(0.7f).align(Alignment.Center)
        )
    }
}

private fun loadImageBitmap(file: File): ImageBitmap? {
    return try {
        SkiaImage.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
    } catch (e: Exception) {
        println("Error loading image for match screen: ${e.message}")
        null
    }
}