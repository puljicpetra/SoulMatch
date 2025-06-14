package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData

@Composable
fun LoginScreen(onNavigate: (Screen) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign in", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("to your account", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email*") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password*") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val cleanEmail = email.trim().lowercase()

                if (cleanEmail.isBlank() || password.isBlank()) {
                    dialogMessage = "Email and password cannot be empty."
                    showDialog = true
                    return@Button
                }

                val user = AppData.users.find { it.email == cleanEmail && it.password == password }

                if (user != null) {
                    println("SUCCESS: Logged in as: $user")
                    onNavigate(Screen.FindMatch)
                } else {
                    dialogMessage = "Invalid email or password. Please try again."
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
        ) {
            Text("Sign in", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "New here? Create an account",
            color = Color(0xFFE57373),
            modifier = Modifier.clickable { onNavigate(Screen.Signup) }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Login Error") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}