package com.example.livingai_lg.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ActionPopup(
    visible: Boolean,
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    autoDismissMillis: Long = 5000L,
    onClick: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    // Auto dismiss
    LaunchedEffect(visible) {
        if (visible) {
            delay(autoDismissMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(50))
                .background(backgroundColor, RoundedCornerShape(50))
                .clickable(
                    enabled = onClick != null,
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick?.invoke()
                }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = text,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = contentColor.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDismiss()
                    }
            )
        }
    }
}
