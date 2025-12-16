package com.example.livingai_lg.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.theme.FarmTextDark
import com.example.livingai_lg.ui.theme.FarmTextNormal

@Composable
fun FarmHeader() {
    Row {
        Text(
            text = "Farm",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE17100)
        )
        Text(
            text = "Market",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = FarmTextDark
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Find your perfect livestock",
        fontSize = 16.sp,
        color = FarmTextNormal
    )
}
