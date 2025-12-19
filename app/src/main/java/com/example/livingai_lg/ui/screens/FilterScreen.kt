package com.example.livingai_lg.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.components.DropdownInput
import com.example.livingai_lg.ui.components.RangeFilter
import com.example.livingai_lg.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {
    var selectedAnimal by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf("") }
    var selectedDistance by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    var animalExpanded by remember { mutableStateOf(false) }
    var breedExpanded by remember { mutableStateOf(false) }
    var distanceExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    var priceFrom by remember { mutableStateOf("0") }
    var priceTo by remember { mutableStateOf("90,000") }
    var priceSliderValue by remember { mutableFloatStateOf(0f) }

    var ageFrom by remember { mutableStateOf("1") }
    var ageTo by remember { mutableStateOf("20") }

    var selectedPregnancyStatus by remember { mutableStateOf(setOf<String>()) }

    var weightFrom by remember { mutableStateOf("0") }
    var weightTo by remember { mutableStateOf("9000") }

    var milkYieldFrom by remember { mutableStateOf("0") }
    var milkYieldTo by remember { mutableStateOf("900") }

    var calvingFrom by remember { mutableStateOf(0) }
    var calvingTo by remember { mutableStateOf(10) }

    var calvingFromExpanded by remember { mutableStateOf(false) }
    var calvingToExpanded by remember { mutableStateOf(false) }

    val maxCalving = 10

    val calvingFromOptions = (0..maxCalving).map { it.toString() }
    val calvingToOptions = (calvingFrom..maxCalving).map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0A0A0A)
                        )
                    }

                    Text(
                        text = "Filters",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Animal Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropdownInput(
                    label = "Animal",
                    selected = selectedAnimal,
                    options = listOf("Cow", "Buffalo", "Goat", "Sheep"),
                    expanded = animalExpanded,
                    onExpandedChange = { animalExpanded = it },
                    onSelect = { item ->
                        selectedAnimal = item
                        animalExpanded = false
                    },
                    placeholder = "Select Animal",   // <--- half width
                )
            }

            // Breed Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropdownInput(
                    label = "Breed",
                    selected = selectedBreed,
                    options = listOf("Holstein", "Jersey", "Gir", "Sahiwal"),
                    expanded = breedExpanded,
                    onExpandedChange = { breedExpanded = it },
                    onSelect = { item ->
                        selectedBreed = item
                        breedExpanded = false
                    },
                    placeholder = "Select Breed",   // <--- half width
                )
            }

            // Price and Age Row

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(), // 👈 important
                        label = "Price",
                        min = 0,
                        max = 90_000,
                        valueFrom = priceFrom.toInt(),
                        valueTo = priceTo.replace(",", "").toInt(),
                        onValueChange = { from, to ->
                            priceFrom = from.toString()
                            priceTo = to.toString()
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Age",
                        min = 0,
                        max = 20,
                        valueFrom = ageFrom.toInt(),
                        valueTo = ageTo.replace(",", "").toInt(),
                        showSlider = false,
                        onValueChange = { from, to ->
                            ageFrom = from.toString()
                            ageTo = to.toString()
                        }
                    )
                }


            // Distance and Gender Row

                // Distance Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownInput(
                        label = "Distance",
                        selected = selectedDistance,
                        options = listOf("0-5 km", "5-10 km", "10-20 km", "20+ km"),
                        expanded = distanceExpanded,
                        onExpandedChange = { distanceExpanded = it },
                        onSelect = { item ->
                            selectedDistance = item
                            distanceExpanded = false
                        },
                        placeholder = "Choose Distance",   // <--- half width
                    )
                }

                // Gender Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownInput(
                        label = "Gender",
                        selected = selectedGender,
                        options = listOf("Male", "Female"),
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it },
                        onSelect = { item ->
                            selectedGender = item
                            genderExpanded = false
                        },
                        placeholder = "Choose Gender",   // <--- half width
                    )
                }




            // Pregnancy Status Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Pregnancy Status",
                    fontSize = AppTypography.Body,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF364153)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    listOf("Pregnant", "Calved", "Option").forEach { status ->
                        PregnancyStatusChip(
                            label = status,
                            isSelected = selectedPregnancyStatus.contains(status),
                            onClick = {
                                selectedPregnancyStatus = if (selectedPregnancyStatus.contains(status)) {
                                    selectedPregnancyStatus - status
                                } else {
                                    selectedPregnancyStatus + status
                                }
                            }
                        )
                    }
                }
            }


                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(), // 👈 important
                        label = "Weight",
                        min = 0,
                        max = 9000,
                        valueFrom = weightFrom.toInt(),
                        valueTo = weightTo.replace(",", "").toInt(),
                        onValueChange = { from, to ->
                            weightFrom = from.toString()
                            weightTo = to.toString()
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Milk Yield",
                        min = 0,
                        max = 900,
                        valueFrom = milkYieldFrom.toInt(),
                        valueTo = milkYieldTo.replace(",", "").toInt(),
                        showSlider = true,
                        onValueChange = { from, to ->
                            milkYieldFrom = from.toString()
                            milkYieldTo = to.toString()
                        }
                    )
                }


            // Calving Number Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // FROM
                    DropdownInput(
                        label = "Calving Number",
                        selected = calvingFrom.toString(),
                        options = calvingFromOptions,
                        expanded = calvingFromExpanded,
                        onExpandedChange = { calvingFromExpanded = it },
                        onSelect = { value ->
                            val newFrom = value.toInt()
                            calvingFrom = newFrom

                            // 👇 enforce invariant
                            if (calvingTo < newFrom) {
                                calvingTo = newFrom
                            }

                            calvingFromExpanded = false
                        },
                        placeholder = "From",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "to",
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    // TO
                    DropdownInput(
                        selected = calvingTo.toString(),
                        options = calvingToOptions, // 👈 constrained options
                        expanded = calvingToExpanded,
                        onExpandedChange = { calvingToExpanded = it },
                        onSelect = { value ->
                            calvingTo = value.toInt()
                            calvingToExpanded = false
                        },
                        placeholder = "To",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Submit Button
                Button(
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .width(173.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Submit",
                        fontSize = AppTypography.Body,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Cancel Button
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .width(173.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0x1A000000)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF0A0A0A)
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = AppTypography.Body,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun PregnancyStatusChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(29.dp)
            .widthIn(min = 76.dp)
            .background(
                if (isSelected) Color(0xFF007BFF) else Color.Transparent,
                RoundedCornerShape(24.dp)
            )
            .border(
                1.dp,
                Color(0xFFE5E7EB),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
            onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSelected) Color(0xFFF4F4F4) else Color.Black,
                textDecoration = TextDecoration.Underline
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color(0xFFE3E3E3),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
