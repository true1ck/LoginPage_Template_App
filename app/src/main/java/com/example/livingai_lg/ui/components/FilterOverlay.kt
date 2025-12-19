package com.example.livingai_lg.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSubmitClick: () -> Unit = {},
) {
    if (!visible) return

    BackHandler { onDismiss() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Dimmed background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
        )
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally { fullWidth -> fullWidth },
            exit = slideOutHorizontally { fullWidth -> fullWidth },
            modifier = Modifier.fillMaxHeight()
        ) {
            FilterScreen(
                onBackClick = onDismiss,
                onCancelClick = onDismiss,
                onSubmitClick = {
                    onSubmitClick()
                    onDismiss()
                }
            )
        }
    }
}
