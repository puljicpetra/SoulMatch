package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.ui.composables.Chip
import hr.unipu.java.soulmatch.ui.composables.LikeDislikeButtons
import hr.unipu.java.soulmatch.ui.composables.ProfileImage

@Composable
fun FindMatchScreen(onNavigate: (Screen) -> Unit) {
    val currentUser = AppData.currentUser
    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Not logged in. Please restart.")
        }
        return
    }

    val sortedMatches = remember(AppData.users) {
        AppData.users
            .filter { it.id != currentUser.id }
            .sortedByDescending { otherUser ->
                currentUser.interests.intersect(otherUser.interests.toSet()).size
            }
    }

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
                            }) { Text("My Profile") }
                            DropdownMenuItem(onClick = {
                                menuExpanded = false
                                AppData.currentUser = null
                                onNavigate(Screen.Welcome)
                            }) { Text("Log Out") }
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
            if (sortedMatches.isNotEmpty() && currentIndex < sortedMatches.size) {
                val userToShow = sortedMatches[currentIndex]

                Card(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileImage(userToShow.profilePictureUrl, userToShow.name)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "${userToShow.name}, ${userToShow.age}", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold))

                        if (userToShow.city.isNotBlank() || userToShow.country.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${userToShow.city}, ${userToShow.country}", style = MaterialTheme.typography.body2, color = Color.Gray)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(text = userToShow.bio, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))

                        if (userToShow.interests.isNotEmpty()) {
                            Text("Interests", style = MaterialTheme.typography.h6, modifier = Modifier.padding(top = 8.dp))
                            LazyRow(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                items(userToShow.interests) { interest ->
                                    Chip(interest)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LikeDislikeButtons(
                    onDislike = {
                        println("Declined user: ${userToShow.name}")
                        currentIndex++
                    },
                    onLike = {
                        println("Liked user: ${userToShow.name}")
                        // TODO: Implementirati logiku za lajkanje i provjeru matcha
                        currentIndex++
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No more users to show. Come back later!", fontSize = 18.sp)
                }
            }
        }
    }
}