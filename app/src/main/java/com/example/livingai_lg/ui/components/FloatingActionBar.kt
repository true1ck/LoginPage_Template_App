package com.example.livingai_lg.ui.components

import android.media.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.livingai_lg.R

@Composable
fun FloatingActionBar(
    modifier: Modifier = Modifier,
    showContainer: Boolean = true,
    onChatClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {}
) {
    val containerModifier =
        if (showContainer) {
            Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(50),
                    clip = false
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 12.dp, vertical = 12.dp)
        } else {
            Modifier // no background, no shadow
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.padding(horizontal = 24.dp)
            .then(containerModifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionIcon(Icons.AutoMirrored.Outlined.Chat, onChatClick)
            FloatingActionIcon(Icons.Outlined.Phone, onCallClick)
            FloatingActionIcon(Icons.Outlined.LocationOn, onLocationClick)
            FloatingActionIcon(Icons.Outlined.BookmarkAdd, onBookmarkClick)
        }
    }
}


@Composable
private fun FloatingActionIcon(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0A0A0A),
            modifier = Modifier.size(22.dp)
        )
    }
}

