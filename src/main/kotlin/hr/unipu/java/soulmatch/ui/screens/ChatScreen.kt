package hr.unipu.java.soulmatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.unipu.java.soulmatch.model.AppData
import hr.unipu.java.soulmatch.model.Message
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

@Composable
fun ChatScreen(conversationId: String, onNavigateBack: () -> Unit) {
    val currentUser = AppData.currentUser!!
    val conversation = AppData.conversations.find { it.id == conversationId }

    if (conversation == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Conversation not found.")
        }
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    val otherUserId = conversation.participantIds.first { it != currentUser.id }
    val otherUser = AppData.users.find { it.id == otherUserId }!!

    var messages by remember {
        mutableStateOf(
            AppData.messages
                .filter { it.conversationId == conversationId }
                .sortedBy { it.timestamp }
        )
    }

    var textState by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(otherUser.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message, message.senderId == currentUser.id)
                }
            }

            LaunchedEffect(messages.size) {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (textState.isNotBlank()) {
                            val newMessage = Message(
                                conversationId = conversationId,
                                senderId = currentUser.id,
                                content = textState.trim()
                            )
                            AppData.messages.add(newMessage)
                            conversation.messageIds.add(newMessage.id)
                            conversation.lastActivityTimestamp = System.currentTimeMillis()

                            messages = messages + newMessage
                            textState = ""

                            AppData.saveAllData()

                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size)
                            }
                        }
                    },
                    enabled = textState.isNotBlank(),
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isFromCurrentUser: Boolean) {
    val alignment = if (isFromCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isFromCurrentUser) Color(0xFFE57373) else Color(0xFFE0E0E0)
    val textColor = if (isFromCurrentUser) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromCurrentUser) 16.dp else 0.dp,
                        bottomEnd = if (isFromCurrentUser) 0.dp else 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(message.content, color = textColor)
        }
    }
}