package com.example.livingai_lg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun RangeFilter(
    modifier: Modifier = Modifier,

    label: String,
    min: Int,
    max: Int,
    valueFrom: Int,
    valueTo: Int,
    modified: Boolean = false,
    onValueChange: (from: Int, to: Int) -> Unit,
    showSlider: Boolean = true,
    valueFormatter: (Int) -> String = { it.toString() }
) {
    var fromValue by remember(valueFrom) { mutableStateOf(valueFrom) }
    var toValue by remember(valueTo) { mutableStateOf(valueTo) }

    Column(modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Label
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF364153)
        )

        // Slider (optional)
        if (showSlider) {
            RangeSlider(
                value = fromValue.toFloat()..toValue.toFloat(),
                onValueChange = { range ->
                    fromValue = range.start.roundToInt()
                        .coerceIn(min, toValue)
                    toValue = range.endInclusive.roundToInt()
                        .coerceIn(fromValue, max)

                    onValueChange(fromValue, toValue)
                },
                valueRange = min.toFloat()..max.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFD9D9D9),
                    activeTrackColor = Color(0xFFD9D9D9),
                    inactiveTrackColor = Color(0xFFE5E7EB)
                )
            )
        }

        // Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RangePill(
                modifier = Modifier.weight(1f),
                value = fromValue,
                modified = modified,
                onValueChange = { newFrom ->
                    val safeFrom = newFrom.coerceIn(min, toValue)
                    fromValue = safeFrom
                    onValueChange(safeFrom, toValue)
                },
                formatter = valueFormatter
            )

            Text("to", fontSize = 15.sp)

            RangePill(
                modifier = Modifier.weight(1f),
                value = toValue,
                modified = modified,
                onValueChange = { newTo ->
                    val safeTo = newTo.coerceIn(fromValue, max)
                    toValue = safeTo
                    onValueChange(fromValue, safeTo)
                },
                formatter = valueFormatter
            )
        }

    }
}

@Composable
private fun RangePill(
    modifier: Modifier = Modifier,
    value: Int,
    modified: Boolean = false,
    onValueChange: (Int) -> Unit,
    formatter: (Int) -> String
) {
    var text by remember(value) {
        mutableStateOf(formatter(value))
    }

    Box(
        modifier = modifier
            .height(30.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0x12000000), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        var modified = modified;
        BasicTextField(
            value = text,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                text = digits
                digits.toIntOrNull()?.let(onValueChange)
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = if(modified) Color.Black else Color(0xFF99A1AF)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
