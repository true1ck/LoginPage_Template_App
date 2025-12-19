package com.example.livingai_lg.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.livingai_lg.ui.models.SortDirection
import com.example.livingai_lg.ui.models.SortField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.components.SortItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortScreen(
    onBackClick: () -> Unit = {},
    onApplyClick: (List<SortField>) -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    var fields by remember {
        mutableStateOf(
            listOf(
                SortField("distance", "Distance"),
                SortField("price", "Price"),
                SortField("milk", "Milk Capacity"),
                SortField("ai", "AI Score"),
                SortField("age", "Age")
            )
        )
    }

    fun toggleField(index: Int) {
        val current = fields[index]

        val nextDirection = when (current.direction) {
            SortDirection.NONE -> SortDirection.ASC
            SortDirection.ASC -> SortDirection.DESC
            SortDirection.DESC -> SortDirection.NONE
        }

        val updated = fields.toMutableList()

        updated[index] = current.copy(
            direction = nextDirection,
            order = if (nextDirection == SortDirection.NONE) null else current.order
        )

        // Recalculate sort order
        val active = updated
            .filter { it.direction != SortDirection.NONE }
            .sortedBy { it.order ?: Int.MAX_VALUE }
            .mapIndexed { i, item -> item.copy(order = i + 1) }

        fields = updated.map { field ->
            active.find { it.key == field.key } ?: field.copy(order = null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0A0A0A)
                    )
                }
                Text("Sort By", fontSize = 32.sp)
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            fields.forEachIndexed { index, field ->
                SortItem(
                    field = field,
                    onToggle = { toggleField(index) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onApplyClick(fields.filter { it.direction != SortDirection.NONE }) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(173.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Apply")
            }

            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(173.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF0A0A0A)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
