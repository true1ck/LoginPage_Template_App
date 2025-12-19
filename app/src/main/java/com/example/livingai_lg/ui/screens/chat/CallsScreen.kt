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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CallRecord(
    val id: String,
    val name: String,
    val initials: String,
    val callType: CallType,
    val duration: String,
    val timestamp: String,
    val isVideoCall: Boolean = false
)

enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED
}

@Composable
fun CallsScreen(
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCallItemClick: (String) -> Unit = {},
    onTabClick: (route: String) -> Unit = {}
) {
    val callHistory = listOf(
        CallRecord(
            id = "1",
            name = "Farmer Kumar",
            initials = "FK",
            callType = CallType.INCOMING,
            duration = "5m 23s",
            timestamp = "Today, 2:30 PM"
        ),
        CallRecord(
            id = "2",
            name = "Seller Raj",
            initials = "SR",
            callType = CallType.OUTGOING,
            duration = "2m 10s",
            timestamp = "Today, 11:45 AM"
        ),
        CallRecord(
            id = "3",
            name = "Buyer Priya",
            initials = "BP",
            callType = CallType.MISSED,
            duration = "",
            timestamp = "Yesterday, 8:15 PM"
        ),
        CallRecord(
            id = "4",
            name = "Seller 1",
            initials = "S1",
            callType = CallType.OUTGOING,
            duration = "8m 45s",
            timestamp = "Yesterday, 5:30 PM",
            isVideoCall = true
        ),
        CallRecord(
            id = "5",
            name = "Veterinarian",
            initials = "V",
            callType = CallType.INCOMING,
            duration = "12m 30s",
            timestamp = "2 days ago"
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
            CallsHeader(
                onBackClick = onBackClick,
                onCallClick = onCallClick,
                onMenuClick = onMenuClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(callHistory) { call ->
                    CallHistoryItem(
                        call = call,
                        onClick = { onCallItemClick(call.id) }
                    )
                }
            }

            ContactsBottomNav(
                currentTab = ContactsTab.CALLS,
                onTabClick = onTabClick
            )
        }
    }
}

@Composable
fun CallsHeader(
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
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
                    text = "Calls",
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
                    imageVector = Icons.Default.Phone,
                    contentDescription = "New Call",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onCallClick() }
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
fun CallHistoryItem(
    call: CallRecord,
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
                    text = call.initials,
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
                    text = call.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E2939),
                    lineHeight = 20.sp,
                    letterSpacing = (-0.312).sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (call.callType) {
                            CallType.INCOMING -> Icons.AutoMirrored.Filled.CallReceived
                            CallType.OUTGOING -> Icons.AutoMirrored.Filled.CallMade
                            CallType.MISSED -> Icons.AutoMirrored.Filled.CallMissed
                        },
                        contentDescription = call.callType.name,
                        tint = when (call.callType) {
                            CallType.INCOMING -> Color(0xFF00A63E)
                            CallType.OUTGOING -> Color(0xFF155DFC)
                            CallType.MISSED -> Color(0xFFE7000B)
                        },
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = when (call.callType) {
                            CallType.INCOMING -> "Incoming • ${call.duration}"
                            CallType.OUTGOING -> "Outgoing • ${call.duration}"
                            CallType.MISSED -> "Missed"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF717182),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = call.timestamp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF717182),
                lineHeight = 16.sp
            )

            Icon(
                imageVector = if (call.isVideoCall) Icons.Default.Videocam else Icons.Default.Phone,
                contentDescription = if (call.isVideoCall) "Video Call" else "Voice Call",
                tint = Color(0xFF030213),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
