package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.livingai_lg.ui.components.AdSpaceBanner
import com.example.livingai_lg.ui.components.AnimalTypeSelector
import com.example.livingai_lg.ui.components.BuyAnimalCard
import com.example.livingai_lg.ui.models.animalTypes

import com.example.livingai_lg.ui.components.FilterButton
import com.example.livingai_lg.ui.components.SortButton
import com.example.livingai_lg.ui.components.UserLocationHeader
import com.example.livingai_lg.ui.layout.BottomNavScaffold
import com.example.livingai_lg.ui.models.mainBottomNavItems
import com.example.livingai_lg.ui.models.sampleAnimals
import com.example.livingai_lg.ui.models.userProfile
import com.example.livingai_lg.R

@Composable
fun BuyScreen(
    onProductClick: (productId: String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavClick: (route: String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onSellerClick: (sellerId: String) -> Unit = {},
) {
    val selectedAnimalType = remember { mutableStateOf<String?>(null) }
    val isSaved = remember { mutableStateOf(false) }





    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {

        BottomNavScaffold(
            items = mainBottomNavItems,
            currentItem = "Home",
            onBottomNavItemClick = onNavClick,
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F4EE))
                    .padding(paddingValues)
            ) {
                item {
                    // Header with profile and notification
                    // Top header strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF7F4EE))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        UserLocationHeader(
                            user = userProfile,
                            onAddressSelected = {
                                // optional: reload listings, persist selection, etc.
                            },
                            onAddNewClick = {}
                        )

                        // Right-side actions (notifications, etc.)
                        Icon(
                            painter = painterResource(R.drawable.ic_notification_unread),
                            contentDescription = "Notifications",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        UserLocationHeader(
//                            user = userProfile,
//                            modifier = Modifier.padding(horizontal = 16.dp)
//                        )
//
//                        Icon(
//                            painter = painterResource(R.drawable.ic_notification_unread),
//                            contentDescription = "Notifications",
//                            tint = Color.Black,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }

                    // Animal type filter buttons
                    AnimalTypeSelector(
                        animalTypes = animalTypes,
                        selectedAnimalType = selectedAnimalType
                    ) { }

                    // Ad space banner
                    AdSpaceBanner(
                        modifier = Modifier.padding(horizontal = 22.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    // Sort and Filter buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SortButton(onSortClick)
                        FilterButton(onFilterClick)
                    }

                    sampleAnimals.forEach { animal ->
                        Spacer(modifier = Modifier.height(16.dp))

                        // Product card
                        BuyAnimalCard(
                            product = animal,
                            isSaved = isSaved.value,
                            onSavedChange = { isSaved.value = it },
                            onProductClick = { onProductClick(animal.id)},
                            onSellerClick = onSellerClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Ad space banner at bottom
                        AdSpaceBanner(
                            modifier = Modifier.padding(horizontal = 22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(80.dp))


                }
            }
        }


    }
}





