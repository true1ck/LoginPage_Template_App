package com.example.livingai_lg.ui.components.backgrounds

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.min
import com.example.livingai_lg.R


@Composable
fun DecorativeBackground() {

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenW = maxWidth.value
        val screenH = maxHeight.value

        // Original bg image design size (YOUR asset’s intended size)
        val designW = 393f
        val designH = 852f

        // Scale factor that preserves aspect ratio
        val scale = min(screenW / designW, screenH / designH)

        //------------------------------
        // Helper to scale dp offsets
        //------------------------------
        fun s(value: Float) = (value * scale).dp

        //------------------------------
        // Background Image
        //------------------------------
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop   // ensures full-screen coverage
        )

        //------------------------------
        // Decorative Elements (scaled)
        //------------------------------

        // 🐐 Goat
        ScaledEmoji(
            emoji = "🐐",
            baseFontSize = 48.sp,
            offsetX = 250f,
            offsetY = 160f,
            scale = scale,
            alpha = 0.10f
        )

        // 🐄 Cow
        ScaledEmoji(
            emoji = "🐄",
            baseFontSize = 60.sp,
            offsetX = 64f,
            offsetY = 569f,
            scale = scale,
            alpha = 0.12f
        )

        // 🌾 Wheat
        ScaledEmoji(
            emoji = "🌾🌾🌾",
            baseFontSize = 32.sp,
            offsetX = 48f,
            offsetY = 730f,
            scale = scale,
            alpha = 0.15f
        )
    }
}

@Composable
private fun ScaledEmoji(
    emoji: String,
    baseFontSize: TextUnit,
    offsetX: Float,
    offsetY: Float,
    scale: Float,
    alpha: Float
) {
    Text(
        text = emoji,
        fontSize = (baseFontSize.value * scale).sp,
        modifier = Modifier
            .offset(
                x = (offsetX * scale).dp,
                y = (offsetY * scale).dp
            )
            .alpha(alpha)
    )
}
