package com.example.livingai_lg.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.livingai_lg.ui.models.SortField
import com.example.livingai_lg.ui.screens.FilterScreen
import com.example.livingai_lg.ui.screens.SortScreen

@Composable
fun SortOverlay(
    visible: Boolean,
    onApplyClick: (selected: List<SortField>) -> Unit,
    onDismiss: () -> Unit,
) {
    BackHandler(enabled = visible) { onDismiss() }

    Box(modifier = Modifier.fillMaxSize()) {

        // Dim background
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

        // Slide-in panel from LEFT
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { -it }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it }
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.85f)
                    .background(Color(0xFFF7F4EE))
                    .clip(
                        RoundedCornerShape(
                            topEnd = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
            ) {
                SortScreen(
                    onApplyClick = { selected ->
                        onApplyClick(selected)
                        onDismiss()
                    },
                    onCancelClick = onDismiss,
                    onBackClick = onDismiss
                )
            }
        }
    }
}
