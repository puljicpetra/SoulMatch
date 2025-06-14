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
import hr.unipu.java.soulmatch.model.User

@Composable
fun SignupScreen(onNavigate: (Screen) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isErrorDialog by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign up", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Create an account", fontSize = 18.sp, color = Color.Gray)
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
            label = { Text("Choose password*") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Repeat password*") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val cleanEmail = email.trim().lowercase()

                if (cleanEmail.isBlank() || password.isBlank()) {
                    dialogMessage = "Email and password cannot be empty."
                    isErrorDialog = true
                    showDialog = true
                    return@Button
                }
                if (!cleanEmail.contains("@") || !cleanEmail.substringAfter("@").contains(".")) {
                    dialogMessage = "Please enter a valid email address."
                    isErrorDialog = true
                    showDialog = true
                    return@Button
                }
                if (password.length < 8) {
                    dialogMessage = "Password must be at least 8 characters long."
                    isErrorDialog = true
                    showDialog = true
                    return@Button
                }
                if (password != repeatPassword) {
                    dialogMessage = "Passwords do not match."
                    isErrorDialog = true
                    showDialog = true
                    return@Button
                }
                if (AppData.users.any { it.email == cleanEmail }) {
                    dialogMessage = "A user with this email already exists."
                    isErrorDialog = true
                    showDialog = true
                    return@Button
                }

                val newUser = User(email = cleanEmail, password = password)
                AppData.users.add(newUser)

                AppData.currentUser = newUser

                AppData.saveUsers()

                println("SUCCESS: User registered: $newUser")
                println("Current user is: ${AppData.currentUser}")

                dialogMessage = "Registration successful!"
                isErrorDialog = false
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
        ) {
            Text("Sign up", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Already have an account? Log in",
            color = Color(0xFFE57373),
            modifier = Modifier.clickable { onNavigate(Screen.Login) }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    if (!isErrorDialog) {
                        onNavigate(Screen.ProfileSetup)
                    }
                },
                title = { Text(if (isErrorDialog) "Registration Error" else "Success") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            if (!isErrorDialog) {
                                onNavigate(Screen.ProfileSetup)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}