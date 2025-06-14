package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData

@Composable
fun ProfileSetupScreen(onNavigate: (Screen) -> Unit) {
    val currentUser = AppData.currentUser
    if (currentUser == null) {
        Text("Error: No user is logged in. Please restart the app.")
        return
    }


    var name by remember { mutableStateOf(currentUser.name) }
    var age by remember { mutableStateOf(currentUser.age.toString()) }
    var bio by remember { mutableStateOf(currentUser.bio) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Set Up Your Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Let others get to know you!",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    age = it
                }
            },
            label = { Text("Your Age") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("About yourself...") },
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )

        // TODO: Gumb za dodavanje slika će doći ovdje

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                currentUser.name = name
                currentUser.age = age.toIntOrNull() ?: 0
                currentUser.bio = bio

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
}