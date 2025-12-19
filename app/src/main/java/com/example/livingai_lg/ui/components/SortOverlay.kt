package com.example.livingai_lg.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.livingai_lg.ui.models.SortField
import com.example.livingai_lg.ui.screens.SortScreen

@Composable
fun SortOverlay(
    visible: Boolean,
    onApplyClick: (selected: List<SortField>) -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() } // tap outside closes
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { -it }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f) // 👈 DOES NOT cover full screen
                    .background(Color(0xFFF7F4EE))
            ) {
                // Prevent click-through
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {}
                ) {
                    SortScreen(
                        onApplyClick = { selected ->
                            onApplyClick(selected)
                            onDismiss()
                            // TODO: apply sort
                        },
                        onCancelClick = onDismiss,
                        onBackClick = onDismiss
                    )
                }
            }
        }
    }
}
