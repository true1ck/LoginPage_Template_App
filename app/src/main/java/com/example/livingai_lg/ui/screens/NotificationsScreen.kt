package com.example.livingai_lg.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.models.AppNotification

@Composable
fun NotificationsScreen(
    notifications: List<AppNotification>,
    onBackClick: () -> Unit,
    onDismiss: (String) -> Unit,
    onNotificationClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Notifications",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }

        Divider(color = Color(0xFFE5E7EB))

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notifications",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = notifications,
                    key = { it.id }
                ) { notification ->
                    NotificationItem(
                        notification = notification,
                        onDismiss = onDismiss,
                        onClick = onNotificationClick
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: AppNotification,
    onDismiss: (String) -> Unit,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .clickable { onClick(notification.id) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = notification.icon,
            contentDescription = null,
            tint = Color(0xFF0A0A0A),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = notification.title,
            fontSize = 14.sp,
            color = Color(0xFF1F2937),
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { onDismiss(notification.id) },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

