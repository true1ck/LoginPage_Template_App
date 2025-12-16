package com.example.livingai_lg.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.components.MediaPickerCard
import com.example.livingai_lg.ui.models.NewListingFormState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewListingScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    formState: NewListingFormState = remember { NewListingFormState() }
) {
    var name by formState.name
    var animal by formState.animal
    var breed by formState.breed
    var age by formState.age
    var milkYield by formState.milkYield
    var calvingNumber by formState.calvingNumber
    var reproductiveStatus by formState.reproductiveStatus
    var description by formState.description

    var animalExpanded by formState.animalExpanded
    var breedExpanded by formState.breedExpanded

    val mediaUploads = formState.mediaUploads

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "New Listing",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0A0A0A)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF7F4EE)
            )
        )

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text(
                        "Name",
                        color = Color(0xFF717182),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0x1A000000),
                    focusedBorderColor = Color(0x1A000000)
                ),
                singleLine = true
            )

            // Animal and Breed Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Animal Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = animalExpanded,
                        onExpandedChange = { animalExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = animal,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    "Animal",
                                    color = Color(0xFF717182),
                                    fontSize = 16.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = Color(0xFFA5A5A5)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0x1A000000),
                                focusedBorderColor = Color(0x1A000000)
                            ),
                            singleLine = true
                        )
                        ExposedDropdownMenu(
                            expanded = animalExpanded,
                            onDismissRequest = { animalExpanded = false }
                        ) {
                            listOf("Cow", "Buffalo", "Goat", "Sheep").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        animal = option
                                        animalExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Breed Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = breedExpanded,
                        onExpandedChange = { breedExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = breed,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    "Breed",
                                    color = Color(0xFF717182),
                                    fontSize = 16.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = Color(0xFFA5A5A5)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0x1A000000),
                                focusedBorderColor = Color(0x1A000000)
                            ),
                            singleLine = true
                        )
                        ExposedDropdownMenu(
                            expanded = breedExpanded,
                            onDismissRequest = { breedExpanded = false }
                        ) {
                            listOf("Holstein", "Jersey", "Gir", "Sahiwal").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        breed = option
                                        breedExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Age and Milk Yield Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    placeholder = {
                        Text(
                            "Age (Years)",
                            color = Color(0xFF717182),
                            fontSize = 16.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Sort",
                            tint = Color(0xFF353535)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0x1A000000),
                        focusedBorderColor = Color(0x1A000000)
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = milkYield,
                    onValueChange = { milkYield = it },
                    placeholder = {
                        Text(
                            "Average Milk Yield (L)",
                            color = Color(0xFF717182),
                            fontSize = 16.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0x1A000000),
                        focusedBorderColor = Color(0x1A000000)
                    ),
                    singleLine = true
                )
            }

            // Calving Number
            OutlinedTextField(
                value = calvingNumber,
                onValueChange = { calvingNumber = it },
                placeholder = {
                    Text(
                        "Calving Number",
                        color = Color(0xFF717182),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0x1A000000),
                    focusedBorderColor = Color(0x1A000000)
                ),
                singleLine = true
            )

            // Reproductive Status
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Reproductive Status",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF0A0A0A)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Pregnant", "Calved", "None").forEach { status ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable(
                                indication = LocalIndication.current,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                reproductiveStatus = status
                            }
                        ) {
                            RadioButton(
                                selected = reproductiveStatus == status,
                                onClick = { reproductiveStatus = status },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF717182),
                                    unselectedColor = Color(0xFF717182)
                                )
                            )
                            Text(
                                text = status,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF0A0A0A)
                            )
                        }
                    }
                }
            }

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        "Description",
                        color = Color(0xFF717182),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0x1A000000),
                    focusedBorderColor = Color(0x1A000000)
                ),
                maxLines = 3
            )

            // Upload Media Section
            Text(
                text = "Upload Media",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF000000),
                modifier = Modifier.padding(top = 8.dp)
            )

            // Photo Uploads Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                mediaUploads.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { upload ->
                            MediaPickerCard(
                                upload = upload,
                                modifier = Modifier.weight(1f),
                                onUriSelected = { uri ->
                                    upload.uri = uri
                                }
                            )
                        }
                        // Fill empty space if odd number of items
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .weight(1f)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Save Profile Button
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Save Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
