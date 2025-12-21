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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.example.livingai_lg.ui.components.WishlistNameOverlay
import com.example.livingai_lg.ui.models.FiltersState
import com.example.livingai_lg.ui.models.RangeFilterState
import com.example.livingai_lg.ui.models.TextFilter
import com.example.livingai_lg.ui.models.WishlistEntry
import com.example.livingai_lg.ui.models.WishlistStore
import com.example.livingai_lg.ui.models.isDefault
import com.example.livingai_lg.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    appliedFilters: FiltersState,
    wishlistEditMode: Boolean = false,
    onSubmitClick: (FiltersState) -> Unit,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {
    var filters by remember {
        mutableStateOf(appliedFilters)
    }
    var showWishlistOverlay by remember { mutableStateOf(false) }

    var selectedAnimal =filters.animal
    var selectedBreed = filters.breed
    var selectedDistance = filters.distance
    var selectedGender = filters.gender

    var animalExpanded by remember { mutableStateOf(false) }
    var breedExpanded by remember { mutableStateOf(false) }
    var distanceExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    var price =filters.price
    var age = filters.age
    var weight = filters.weight
    var milkYield = filters.milkYield
    var calving = filters.calving

    var selectedPregnancyStatus = filters.pregnancyStatuses


    var calvingFromExpanded by remember { mutableStateOf(false) }
    var calvingToExpanded by remember { mutableStateOf(false) }

    val maxCalving = 10

    val calvingFromOptions = (0..maxCalving).map { it.toString() }
    val calvingToOptions = (calving.min..maxCalving).map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxHeight()
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
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

                    if(!wishlistEditMode){
                        IconButton(
                            onClick = {
                                if (!filters.isDefault()) {
                                    showWishlistOverlay = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Add to Wishlist"
                            )
                        }
                    }
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
                    selected = if (selectedAnimal.filterSet) selectedAnimal.value else "",
                    options = listOf("Cow", "Buffalo", "Goat", "Sheep"),
                    expanded = animalExpanded,
                    onExpandedChange = { animalExpanded = it },
                    onSelect = { item ->
                        filters = filters.copy(
                            animal = TextFilter(
                                value = item,
                                filterSet = true
                            )
                        )
                        animalExpanded = false
                    },
                    placeholder = "Select Animal",   // <--- half width
                    textColor = if (selectedAnimal.filterSet) Color.Black else Color.Gray
                )
            }

            // Breed Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropdownInput(
                    label = "Breed",
                    selected = if (selectedBreed.filterSet) selectedBreed.value else "",
                    options = listOf("Holstein", "Jersey", "Gir", "Sahiwal"),
                    expanded = breedExpanded,
                    onExpandedChange = { breedExpanded = it },
                    onSelect = { item ->
                        filters = filters.copy(
                            breed = TextFilter(
                                value = item,
                                filterSet = true
                            )
                        )
                        breedExpanded = false
                    },
                    placeholder = "Select Breed",   // <--- half width
                    textColor = if (selectedAnimal.filterSet) Color.Black else Color.Gray
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
                        valueFrom = price.min,
                        valueTo = price.max,
                        modified = price.filterSet,
                        onValueChange = { from, to ->
                            filters = filters.copy(
                                price = RangeFilterState(
                                    min = from,
                                    max = to,
                                    filterSet = true
                                )
                            )
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(), // 👈 important

                        label = "Age (years)",
                        min = 0,
                        max = 20,
                        valueFrom = age.min,
                        valueTo = age.max,
                        showSlider = false,
                        modified = age.filterSet,
                        onValueChange = { from, to ->
                            filters = filters.copy(
                                age = RangeFilterState(
                                    min = from,
                                    max = to,
                                    filterSet = true
                                )
                            )
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
                        selected = if (selectedDistance.filterSet) selectedDistance.value else "",
                        options = listOf("0-5 km", "5-10 km", "10-20 km", "20+ km"),
                        expanded = distanceExpanded,
                        onExpandedChange = { distanceExpanded = it },
                        onSelect = { item ->
                            filters = filters.copy(
                                distance = TextFilter(
                                    value = item,
                                    filterSet = true
                                )
                            )
                            distanceExpanded = false
                        },
                        placeholder = "Choose Distance",   // <--- half width
                        textColor = if (selectedAnimal.filterSet) Color.Black else Color.Gray
                    )
                }

                // Gender Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownInput(
                        label = "Gender",
                        selected = if (selectedGender.filterSet) selectedGender.value else "",
                        options = listOf("Male", "Female"),
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it },
                        onSelect = { item ->
                            filters = filters.copy(
                                gender = TextFilter(
                                    value = item,
                                    filterSet = true
                                )
                            )
                            genderExpanded = false
                        },
                        placeholder = "Choose Gender",   // <--- half width
                        textColor = if (selectedAnimal.filterSet) Color.Black else Color.Gray
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
                                filters = filters.copy(
                                    pregnancyStatuses =
                                        if (filters.pregnancyStatuses.contains(status))
                                            filters.pregnancyStatuses - status
                                        else
                                            filters.pregnancyStatuses + status
                                )
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
                        valueFrom = weight.min,
                        valueTo = weight.max,
                        modified = weight.filterSet,
                        onValueChange = { from, to ->
                            filters = filters.copy(
                                weight = RangeFilterState(
                                    min = from,
                                    max = to,
                                    filterSet = true
                                )
                            )
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RangeFilter(
                        modifier = Modifier.fillMaxWidth(), // 👈 important

                        label = "Milk Yield",
                        min = 0,
                        max = 900,
                        valueFrom = milkYield.min,
                        valueTo = milkYield.max,
                        modified = milkYield.filterSet,
                        onValueChange = { from, to ->
                            filters = filters.copy(
                                milkYield = RangeFilterState(
                                    min = from,
                                    max = to,
                                    filterSet = true
                                )
                            )
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
                        selected = calving.min.toString(),
                        options = calvingFromOptions,
                        expanded = calvingFromExpanded,
                        onExpandedChange = { calvingFromExpanded = it },
                        onSelect = { value ->
                            val newFrom = value.toInt()
                            val newTo = maxOf(calving.max, newFrom)

                            filters = filters.copy(
                                calving = RangeFilterState(
                                    min = newFrom,
                                    max = newTo,
                                    filterSet = true
                                )
                            )

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
                        selected = calving.max.toString(),
                        options = calvingToOptions, // 👈 constrained options
                        expanded = calvingToExpanded,
                        onExpandedChange = { calvingToExpanded = it },
                        onSelect = { value ->
                            val newTo = value.toInt()
                            val newFrom = minOf(calving.min, newTo)

                            filters = filters.copy(
                                calving = RangeFilterState(
                                    min = newFrom,
                                    max = newTo,
                                    filterSet = true
                                )
                            )
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
                    onClick = { onSubmitClick(filters) },
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
    if (showWishlistOverlay) {
        WishlistNameOverlay(
            onDismiss = { showWishlistOverlay = false },
            onSave = { name ->
                WishlistStore.add(
                    WishlistEntry(
                        name = name,
                        filters = filters
                    )
                )
                showWishlistOverlay = false
            }
        )
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
                onClick = onClick
            )
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
