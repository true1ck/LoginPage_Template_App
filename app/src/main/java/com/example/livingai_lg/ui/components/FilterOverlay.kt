package com.example.livingai_lg.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.livingai_lg.ui.models.FiltersState
import com.example.livingai_lg.ui.screens.FilterScreen

@Composable
fun FilterOverlay(
    visible: Boolean,
    appliedFilters: FiltersState,
    onDismiss: () -> Unit,
    onSubmitClick: (filters: FiltersState) -> Unit = {},
) {
    BackHandler(enabled = visible) { onDismiss() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Dimmed background
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() }
            )
        }

        // Sliding panel
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { it } // from right
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it } // to right
            ),
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            FilterScreen(
                appliedFilters,
                onBackClick = onDismiss,
                onCancelClick = onDismiss,
                onSubmitClick = { filters ->
                    onSubmitClick(filters)
                    onDismiss()
                }
            )
        }
    }
}
