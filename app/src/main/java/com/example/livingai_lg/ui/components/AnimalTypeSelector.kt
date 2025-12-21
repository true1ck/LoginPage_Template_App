package com.example.livingai_lg.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.models.AnimalType


@Composable
fun AnimalTypeSelector(
    animalTypes: List<AnimalType>,
    selectedAnimalType: String?,
    onAnimalTypeSelected: (String) -> Unit
) {
    val selectedAnimalType: String = selectedAnimalType ?: ""

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animalTypes.size) { index ->
            AnimalTypeButton(
                animalType = animalTypes[index],
                isSelected = selectedAnimalType == animalTypes[index].id,
                onClick = { onAnimalTypeSelected(animalTypes[index].id) }
            )
        }
    }
}

@Composable
private fun AnimalTypeButton(
    animalType: AnimalType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isSelected) Color(0xFFEDE9FE) else Color.White,
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = animalType.emoji,
                fontSize = 24.sp
            )
        }

        Text(
            text = animalType.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0A0A0A),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
