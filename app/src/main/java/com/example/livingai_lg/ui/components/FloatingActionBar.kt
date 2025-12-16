package com.example.livingai_lg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.livingai_lg.R

@Composable
fun FloatingActionBar(
    modifier: Modifier = Modifier,
    onChatClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.padding(horizontal = 24.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(50), // pill shape
                clip = false
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionIcon(R.drawable.ic_chat, onChatClick)
            FloatingActionIcon(R.drawable.ic_phone, onCallClick)
            FloatingActionIcon(R.drawable.ic_location, onLocationClick)
            FloatingActionIcon(R.drawable.ic_bookmark_plus, onBookmarkClick)
        }
    }
}


@Composable
private fun FloatingActionIcon(
    iconRes: Int,
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
            .background(Color.White, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color(0xFF0A0A0A),
            modifier = Modifier.size(22.dp)
        )
    }
}

