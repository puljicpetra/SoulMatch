package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
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
fun FindMatchScreen(onNavigate: (Screen) -> Unit) {

    val potentialMatches = remember { AppData.users.filter { it.id != AppData.currentUser?.id } }

    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
                    // TODO: Ovdje će ići slika profila
                    // Image(painter = painterResource(userToShow.profilePictureUrl), ...)

                    Text(
                        text = "${userToShow.name}, ${userToShow.age}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userToShow.bio,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    // TODO: Ovdje će ići lista interesa
                    // LazyRow { items(userToShow.interests) { ... } }
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
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, Color.Red)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Decline", tint = Color.Red, modifier = Modifier.size(36.dp))
                }

                Button(
                    onClick = {
                        println("Liked user: ${userToShow.name}")
                        // TODO: Ovdje će ići logika za spremanje lajka
                        currentIndex++
                    },
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
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