package com.example.livingai_lg.ui.components.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.min
import com.example.livingai_lg.R

@Composable
fun StoreBackground() {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
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
            painter = painterResource(R.drawable.bg_shop),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop   // ensures full-screen coverage
        )
    }
}

