package com.example.livingai_lg.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChatMessage(
    val text: String,
    val timestamp: String,
    val isSentByCurrentUser: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    sellerName: String = "Seller 1",
    sellerStatus: String = "Online",
    onBackClick: () -> Unit = {},
    onPhoneClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }

    val messages = remember {
        listOf(
            ChatMessage(
                text = "Hello! Yes, the cow is still available. Would you like to visit?",
                timestamp = "10:30 AM",
                isSentByCurrentUser = false
            ),
            ChatMessage(
                text = "Hey is she still available",
                timestamp = "10:28 AM",
                isSentByCurrentUser = true
            ),
            ChatMessage(
                text = "She's a healthy Gir cow, 2 years old, great milk yield.",
                timestamp = "10:31 AM",
                isSentByCurrentUser = false
            ),
            ChatMessage(
                text = "What's the price?",
                timestamp = "10:32 AM",
                isSentByCurrentUser = true
            ),
            ChatMessage(
                text = "₹45,000. Negotiable if you visit today.",
                timestamp = "10:33 AM",
                isSentByCurrentUser = false
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatHeader(
                sellerName = sellerName,
                sellerStatus = sellerStatus,
                onBackClick = onBackClick,
                onPhoneClick = onPhoneClick,
                onMenuClick = onMenuClick
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
            }

            MessageInputBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    messageText = ""
                }
            )
        }
    }
}

@Composable
fun ChatHeader(
    sellerName: String,
    sellerStatus: String,
    onBackClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0A0A0A),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF0F0F0), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S1",
                        fontSize = 14.sp,
                        color = Color(0xFF0A0A0A)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = sellerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1E2939)
                    )
                    Text(
                        text = sellerStatus,
                        fontSize = 12.sp,
                        color = Color(0xFF4A5565)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onPhoneClick) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color(0xFF0A0A0A),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color(0xFF0A0A0A),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = if (message.isSentByCurrentUser) {
                RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            } else {
                RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            },
            color = if (message.isSentByCurrentUser) Color(0xFF0A0A0A) else Color.White,
            modifier = Modifier
                .widthIn(max = 271.dp)
                .then(
                    if (!message.isSentByCurrentUser) {
                        Modifier.border(
                            width = 1.dp,
                            color = Color(0x1A000000),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                    } else Modifier
                )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = if (message.isSentByCurrentUser) Color.White else Color(0xFF0A0A0A)
                )
                Text(
                    text = message.timestamp,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = if (message.isSentByCurrentUser) Color(0x99FFFFFF) else Color(0xFF717182)
                )
            }
        }
    }
}

@Composable
fun MessageInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val isMultiline = messageText.contains("\n") || messageText.length > 40

    Surface(
        modifier = Modifier.fillMaxWidth().imePadding(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // TEXT INPUT
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(
                        min = if (isMultiline) 44.dp else 36.dp,
                        max = 120.dp
                    ),
                placeholder = {
                    Text(
                        text = "Type a message",
                        fontSize = 14.sp,
                        color = Color(0xFF717182)
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = Color.Black   // ✅ typed text black
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0x1A000000),
                    focusedBorderColor = Color(0x33000000),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = false,
                maxLines = 4
            )

            // ATTACHMENT ICON
            IconButton(
                onClick = { },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Attachment,
                    contentDescription = "Attachment",
                    tint = Color(0xFF717182),
                    modifier = Modifier.size(20.dp)
                )
            }

            // VOICE BUTTON
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF0A0A0A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardVoice,
                        contentDescription = "Voice",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // SEND BUTTON
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF0A0A0A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onSendClick,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
