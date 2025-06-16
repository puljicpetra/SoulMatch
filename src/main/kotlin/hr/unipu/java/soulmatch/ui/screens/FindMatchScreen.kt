package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.People
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun FindMatchScreen(onNavigate: (Screen) -> Unit) {
    val currentUser = AppData.currentUser
    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Not logged in. Please restart.")
        }
        return
    }


    val potentialMatches = remember(AppData.users, currentUser.likes, currentUser.dislikes, currentUser.matches) {
        AppData.users.filter { otherUser ->
            val isNotCurrentUser = otherUser.id != currentUser.id
            val isNotAlreadySwiped = !currentUser.likes.contains(otherUser.id) && !currentUser.dislikes.contains(otherUser.id)
            val currentUserIsSeekingOther = currentUser.seeking.isEmpty() || currentUser.seeking.contains(otherUser.gender)
            val otherUserIsSeekingCurrent = otherUser.seeking.isEmpty() || otherUser.seeking.contains(currentUser.gender)
            val isPreferenceMatch = currentUserIsSeekingOther && otherUserIsSeekingCurrent
            isNotCurrentUser && isNotAlreadySwiped && isPreferenceMatch
        }.sortedByDescending { otherUser ->
            currentUser.interests.intersect(otherUser.interests.toSet()).size
        }
    }

    var currentIndex by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showMatchDialog by remember { mutableStateOf(false) }
    var matchedUserName by remember { mutableStateOf("") }


    if (showMatchDialog) {
        AlertDialog(
            onDismissRequest = { showMatchDialog = false },
            title = { Text("🎉 It's a Match! 🎉", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE57373)) },
            text = { Text("You and $matchedUserName have liked each other. You can now start a conversation!") },
            confirmButton = {
                Button(
                    onClick = { showMatchDialog = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE57373))
                ) {
                    Text("Awesome!", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showMatchDialog = false
                        onNavigate(Screen.Matches)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text("Go to Messages")
                }
            }
        )
    }

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
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 8.dp
            ) {
                BottomNavigationItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.People, "Find Matches") },
                    label = { Text("Find") },
                    selectedContentColor = Color(0xFFE57373),
                    unselectedContentColor = Color.Gray
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = { onNavigate(Screen.Matches) },
                    icon = { Icon(Icons.Default.Forum, "Messages") },
                    label = { Text("Messages") },
                    selectedContentColor = Color(0xFFE57373),
                    unselectedContentColor = Color.Gray
                )
            }
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
                        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
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
                        Text(text = userToShow.bio, style = MaterialTheme.typography.body1)

                        if (userToShow.interests.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Interests", style = MaterialTheme.typography.h6)
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
                        AppData.userDislikes(currentUser, userToShow)
                        currentIndex++
                    },
                    onLike = {
                        val isMatch = AppData.userLikes(currentUser, userToShow)
                        if (isMatch) {
                            matchedUserName = userToShow.name
                            showMatchDialog = true
                        }
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