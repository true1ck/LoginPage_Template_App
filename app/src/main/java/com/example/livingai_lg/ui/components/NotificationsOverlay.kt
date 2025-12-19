package com.example.livingai_lg.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import com.example.livingai_lg.ui.models.AppNotification
import com.example.livingai_lg.ui.screens.NotificationsScreen

@Composable
fun NotificationsOverlay(
    visible: Boolean,
    notifications: List<AppNotification>,
    onClose: () -> Unit,
    onDismiss: (String) -> Unit,
    onNotificationClick: (String) -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        NotificationsScreen(
            notifications = notifications,
            onBackClick = onClose,
            onDismiss = onDismiss,
            onNotificationClick = onNotificationClick
        )
    }
}
