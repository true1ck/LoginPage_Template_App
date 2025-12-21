package com.example.livingai_lg.ui.screens.chat

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmmarketplace.ui.screens.ContactsBottomNav
import com.example.farmmarketplace.ui.screens.ContactsTab
import com.example.livingai_lg.ui.layout.BottomNavScaffold
import com.example.livingai_lg.ui.models.chatBottomNavItems

data class ChatPreview(
    val id: String,
    val name: String,
    val initials: String,
    val lastMessage: String,
    val timestamp: String,
    val isOnline: Boolean = false,
    val unreadCount: Int = 0
)

@Composable
fun ChatsScreen(
    onBackClick: () -> Unit = {},
    onNewChatClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onChatItemClick: (String) -> Unit = {},
    onTabClick: (route: String) -> Unit = {}
) {
    val chatList = listOf(
        ChatPreview(
            id = "1",
            name = "Farmer Kumar",
            initials = "FK",
            lastMessage = "The cows are healthy and ready for viewing",
            timestamp = "Today, 2:30 PM",
            isOnline = true,
            unreadCount = 2
        ),
        ChatPreview(
            id = "2",
            name = "Seller Raj",
            initials = "SR",
            lastMessage = "You: Can you send more photos?",
            timestamp = "Today, 11:45 AM",
            isOnline = true,
            unreadCount = 0
        ),
        ChatPreview(
            id = "3",
            name = "Buyer Priya",
            initials = "BP",
            lastMessage = "What's the best time to visit?",
            timestamp = "Yesterday, 8:15 PM",
            isOnline = false,
            unreadCount = 1
        ),
        ChatPreview(
            id = "4",
            name = "Seller 1",
            initials = "S1",
            lastMessage = "You: Thanks for the information",
            timestamp = "Yesterday, 5:30 PM",
            isOnline = true,
            unreadCount = 0
        ),
        ChatPreview(
            id = "5",
            name = "Veterinarian",
            initials = "V",
            lastMessage = "The animal health check is complete",
            timestamp = "2 days ago",
            isOnline = false,
            unreadCount = 0
        ),
        ChatPreview(
            id = "6",
            name = "Market Vendor",
            initials = "MV",
            lastMessage = "You: Available this weekend?",
            timestamp = "3 days ago",
            isOnline = false,
            unreadCount = 0
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBF8))
    ) {
        BottomNavScaffold(
            items = chatBottomNavItems,
            currentItem = "Chats",
            onBottomNavItemClick = onTabClick,
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                ChatsHeader(
                    onBackClick = onBackClick,
                    onNewChatClick = onNewChatClick,
                    onMenuClick = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatList) { chat ->
                        ChatListItem(
                            chat = chat,
                            onClick = { onChatItemClick(chat.id) }
                        )
                    }
                }

//                ContactsBottomNav(
//                    currentTab = ContactsTab.CHATS,
//                    onTabClick = onTabClick
//                )
            }
        }
    }
}

@Composable
fun ChatsHeader(
    onBackClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f)
            )
            .background(Color(0xFFFCFBF8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(26.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBackClick() }
                )

                Text(
                    text = "Chats",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A),
                    lineHeight = 42.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Chat",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNewChatClick() }
                )

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onMenuClick() }
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: ChatPreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFFFCFBF8), RoundedCornerShape(12.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE5E7EB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.initials,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A),
                    lineHeight = 21.sp
                )

                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .background(Color(0xFF00A63E), CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E2939),
                    lineHeight = 20.sp,
                    letterSpacing = (-0.312).sp
                )

                Text(
                    text = chat.lastMessage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF717182),
                    lineHeight = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = chat.timestamp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF717182),
                lineHeight = 16.sp
            )

            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF155DFC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}