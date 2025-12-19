package com.example.livingai_lg.ui.models

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star

data class AppNotification(
    val id: String,
    val title: String,
    val icon: ImageVector
)

val sampleNotifications = listOf(
    AppNotification(
        id = "1",
        title = "Animal saved successfully",
        icon = Icons.Default.Bookmark
    ),
    AppNotification(
        id = "2",
        title = "New message from Seller",
        icon = Icons.Default.Chat
    ),
    AppNotification(
        id = "3",
        title = "Price dropped on an animal you viewed",
        icon = Icons.Default.Notifications
    ),
    AppNotification(
        id = "4",
        title = "You received a new rating",
        icon = Icons.Default.Star
    )
)
