package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hr.unipu.java.soulmatch.Screen

@Composable
fun ProfileSetupScreen(onNavigate: (Screen) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ovo je Profile Setup Ekran - Uskoro!")
    }
}