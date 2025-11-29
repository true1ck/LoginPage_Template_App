package com.example.livingai_lg.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.theme.LightCream
import com.example.livingai_lg.ui.theme.LighterCream
import com.example.livingai_lg.ui.theme.LightestGreen
import com.example.livingai_lg.ui.theme.LivingAi_LgTheme

@Composable
fun SuccessScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightCream, LighterCream, LightestGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Success!",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your profile has been created.",
                fontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    LivingAi_LgTheme {
        SuccessScreen()
    }
}
