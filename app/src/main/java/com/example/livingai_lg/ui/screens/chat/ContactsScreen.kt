package com.example.farmmarketplace.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.navigation.AppScreen

data class Contact(
    val id: String,
    val name: String,
    val initials: String,
    val status: String,
    val isOnline: Boolean = false,
    val phoneNumber: String? = null
)

enum class ContactsTab {
    CONTACTS,
    CALLS,
    CHATS
}

@Composable
fun ContactsScreen(
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onContactClick: (String) -> Unit = {},
    onCallClick: (String) -> Unit = {},
    onMessageClick: (String) -> Unit = {},
    onTabClick: (route: String) -> Unit = {}
) {
    val contacts = listOf(
        Contact(
            id = "1",
            name = "Farmer Kumar",
            initials = "FK",
            status = "Online",
            isOnline = true
        ),
        Contact(
            id = "2",
            name = "Seller Raj",
            initials = "SR",
            status = "+91 98765 43211",
            isOnline = false,
            phoneNumber = "+91 98765 43211"
        ),
        Contact(
            id = "3",
            name = "Buyer Priya",
            initials = "BP",
            status = "Online",
            isOnline = true
        ),
        Contact(
            id = "4",
            name = "Seller 1",
            initials = "S1",
            status = "+91 98765 43213",
            isOnline = false,
            phoneNumber = "+91 98765 43213"
        ),
        Contact(
            id = "5",
            name = "Veterinarian",
            initials = "V",
            status = "Online",
            isOnline = true
        ),
        Contact(
            id = "6",
            name = "Farm Supply",
            initials = "FS",
            status = "+91 98765 43215",
            isOnline = false,
            phoneNumber = "+91 98765 43215"
        ),
        Contact(
            id = "7",
            name = "Transport Co.",
            initials = "TC",
            status = "+91 98765 43216",
            isOnline = false,
            phoneNumber = "+91 98765 43216"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFBF8))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ContactsHeader(
                onBackClick = onBackClick,
                onSearchClick = onSearchClick,
                onMenuClick = onMenuClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(contacts) { contact ->
                    ContactItem(
                        contact = contact,
                        onContactClick = { onContactClick(contact.id) },
                        onCallClick = { onCallClick(contact.id) },
                        onMessageClick = { onMessageClick(contact.id) }
                    )
                }
            }

            ContactsBottomNav(
                currentTab = ContactsTab.CONTACTS,
                onTabClick = onTabClick
            )
        }
    }
}

@Composable
fun ContactsHeader(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
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
                    text = "Contacts",
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
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onSearchClick() }
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
fun ContactItem(
    contact: Contact,
    onContactClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(73.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.05f)
            )
            .background(Color(0xFFFCFBF8))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onContactClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE5E7EB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.initials,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A),
                    lineHeight = 21.sp
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = contact.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E2939),
                    lineHeight = 20.sp,
                    letterSpacing = (-0.312).sp
                )

                Text(
                    text = contact.status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF717182),
                    lineHeight = 16.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCallClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Call",
                    tint = Color(0xFF030213),
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onMessageClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = "Message",
                    tint = Color(0xFF030213),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ContactsBottomNav(
    currentTab: ContactsTab,
    onTabClick: (route: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFF000000).copy(alpha = 0.1f)
            )
            .background(Color(0xFFFCFBF8))
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ContactsTabItem(
            icon = Icons.AutoMirrored.Filled.Chat,
            label = "Chats",
            isSelected = currentTab == ContactsTab.CHATS,
            onClick = { onTabClick(AppScreen.CHATS) }
        )
        ContactsTabItem(
            icon = Icons.Default.Phone,
            label = "Calls",
            isSelected = currentTab == ContactsTab.CALLS,
            onClick = { onTabClick(AppScreen.CALLS) }
        )
        ContactsTabItem(
            icon = Icons.Default.Contacts,
            label = "Contacts",
            isSelected = currentTab == ContactsTab.CONTACTS,
            onClick = { onTabClick(AppScreen.CONTACTS) }
        )


    }
}

@Composable
fun ContactsTabItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF0A0A0A),
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0A0A0A),
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}