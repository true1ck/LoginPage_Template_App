package com.example.livingai_lg.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.theme.AppTypography
import com.example.livingai_lg.ui.theme.FarmTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(
    label: String? = null,                 // optional label
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit,
    placeholder: String = "Select",        // NEW - custom placeholder
    textColor: Color = Color.Black,
    modifier: Modifier = Modifier          // NEW - allows width control
) {
    Column(
        modifier = modifier,               // <-- now caller can control width
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Optional label
//        if (label != null) {
            Text(
                text = label?:" ",
                fontSize = AppTypography.Body,
                fontWeight = FontWeight.Medium,
                color = FarmTextDark
            )
//        } else {
//            // Reserve label space so layout doesn’t shift
//            Spacer(modifier = Modifier.height(20.dp))  // ← same height as label text line
//        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(!expanded) }
        ) {
            // Anchor box
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    .clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onExpandedChange(true)
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Custom placeholder support
                    Text(
                        text = selected.ifEmpty { placeholder },
                        fontSize = AppTypography.Body,
                        color = if (selected.isEmpty()) Color(0xFF99A1AF) else textColor
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = FarmTextDark
                    )

                }
            }

            // Material3 Dropdown
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(item, fontSize = AppTypography.Body, color = FarmTextDark)
                        },
                        onClick = {
                            onSelect(item)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
