package com.example.livingai_lg.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 14.dp,
    color: Color = Color(0xFFDE9A07)
) {
    val fullStars = rating.toInt()
    val remainder = rating - fullStars

    val showHalfStar = remainder in 0.25f..0.74f
    val extraFullStar = remainder >= 0.75f

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Full stars
        repeat(
            if (extraFullStar) fullStars + 1 else fullStars
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(starSize)
            )
        }

        // Half star (only if not rounded up)
        if (showHalfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
