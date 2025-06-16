package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hr.unipu.java.soulmatch.Screen
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.model.Conversation
import hr.unipu.java.soulmatch.ui.composables.ProfileImage

fun findOrCreateConversation(userId1: String, userId2: String): Conversation {
    val participantIds = setOf(userId1, userId2)
    val existing = AppData.conversations.find { it.participantIds == participantIds }
    if (existing != null) {
        return existing
    }
    val newConversation = Conversation(participantIds = participantIds)
    AppData.conversations.add(newConversation)
    AppData.saveAllData()
    return newConversation
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MatchesScreen(onNavigate: (Screen, String?) -> Unit) {
    val currentUser = AppData.currentUser
    if (currentUser == null) {
        return
    }

    val matches = AppData.users.filter { it.id in currentUser.matches }

    val conversations = AppData.conversations
        .filter { currentUser.id in it.participantIds }
        .sortedByDescending { it.lastActivityTimestamp }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Razgovori") },
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 8.dp
            ) {
                BottomNavigationItem(
                    selected = false,
                    onClick = { onNavigate(Screen.FindMatch, null) },
                    icon = { Icon(Icons.Default.People, "Find Matches") },
                    label = { Text("Find") },
                    selectedContentColor = Color(0xFFE57373),
                    unselectedContentColor = Color.Gray
                )
                BottomNavigationItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Forum, "Messages") },
                    label = { Text("Messages") },
                    selectedContentColor = Color(0xFFE57373),
                    unselectedContentColor = Color.Gray
                )
            }
        }
    ) { padding ->
        if (matches.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No matches yet.", style = MaterialTheme.typography.h6)
                    Text("Go like some people to find your soulmate!", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {

                item {
                    Text(
                        text = "Recent Matches",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(matches) { matchUser ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.clickable {
                                    val conversation = findOrCreateConversation(currentUser.id, matchUser.id)
                                    onNavigate(Screen.Chat, conversation.id)
                                }
                            ) {
                                Box(modifier = Modifier.size(72.dp)) {
                                    ProfileImage(url = matchUser.profilePictureUrl, name = matchUser.name)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(matchUser.name, style = MaterialTheme.typography.body2)
                            }
                        }
                    }
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }

                item {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                if (conversations.isEmpty()) {
                    item {
                        Box(
                            Modifier.fillParentMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No messages yet. Tap a match to start!", color = Color.Gray)
                        }
                    }
                } else {
                    items(conversations) { conversation ->
                        val otherUserId = conversation.participantIds.first { it != currentUser.id }
                        val otherUser = AppData.users.find { it.id == otherUserId }

                        if (otherUser != null) {
                            val lastMessage = AppData.messages
                                .filter { it.id in conversation.messageIds }
                                .maxByOrNull { it.timestamp }

                            ListItem(
                                modifier = Modifier.clickable {
                                    onNavigate(Screen.Chat, conversation.id)
                                },
                                icon = {
                                    Box(modifier = Modifier.size(56.dp)) {
                                        ProfileImage(url = otherUser.profilePictureUrl, name = otherUser.name)
                                    }
                                },
                                text = { Text(otherUser.name, fontWeight = FontWeight.Bold) },
                                secondaryText = {
                                    Text(
                                        lastMessage?.content ?: "Matched! Start the conversation!",
                                        maxLines = 1,
                                        color = Color.Gray
                                    )
                                }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}