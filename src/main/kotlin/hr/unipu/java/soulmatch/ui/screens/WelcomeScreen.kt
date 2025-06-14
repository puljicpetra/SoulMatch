package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.unipu.java.soulmatch.Screen

@Composable
fun WelcomeScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Image(
            painter = painterResource("JVM_Logo.png"),
            contentDescription = "SoulMatch Logo"
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome to the App", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Discover. Like. Match.", fontSize = 18.sp)
        }

        Button(
            onClick = { onNavigate(Screen.Login) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White
            )
        ) {
            Text("Next")
        }
    }
}