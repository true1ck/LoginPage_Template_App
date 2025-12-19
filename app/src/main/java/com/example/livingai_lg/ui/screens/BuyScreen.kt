package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.livingai_lg.ui.components.ActionPopup
import com.example.livingai_lg.ui.components.AddressSelectorOverlay
import com.example.livingai_lg.ui.components.FilterOverlay
import com.example.livingai_lg.ui.components.NotificationsOverlay
import com.example.livingai_lg.ui.components.SortOverlay
import com.example.livingai_lg.ui.models.sampleNotifications
import com.example.livingai_lg.ui.navigation.AppScreen

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
    var showAddressSelector by remember { mutableStateOf(false) }
    var selectedAddressId by remember { mutableStateOf<String?>(userProfile.addresses.find { address -> address.isPrimary }?.id) }
    val showFilterOverlay = remember { mutableStateOf(false) }
    val showSortOverlay = remember { mutableStateOf(false) }
    var showSavedPopup by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    val sampleNotifications = sampleNotifications

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
                            onOpenAddressOverlay = { showAddressSelector = true },
                            selectedAddressId = selectedAddressId?:"1",
                        )

                        // Right-side actions (notifications, etc.)
                        Icon(
                            painter = painterResource(R.drawable.ic_notification_unread),
                            contentDescription = "Notifications",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                                .clickable(
                                indication = LocalIndication.current,
                                interactionSource = remember { MutableInteractionSource() }
                            ){ showNotifications = true }
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
                        SortButton(
                            onClick = { showSortOverlay.value = true }
                        )
                        FilterButton(onClick = { showFilterOverlay.value = true })
                    }

                    sampleAnimals.forEach { animal ->
                        Spacer(modifier = Modifier.height(16.dp))

                        // Product card
                        BuyAnimalCard(
                            product = animal,
                            isSaved = isSaved.value,
                            onSavedChange = { isSaved.value = it },
                            onProductClick = { onProductClick(animal.id)},
                            onSellerClick = onSellerClick,
                            onBookmarkClick = { showSavedPopup = true }
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

        AddressSelectorOverlay(
            visible = showAddressSelector,
            addresses = userProfile.addresses,
            selectedAddressId = selectedAddressId,
            onSelect = { addressId ->
                selectedAddressId = addressId
                showAddressSelector = false
            },
            onClose = { showAddressSelector = false }
        )

        SortOverlay(
            visible = showSortOverlay.value,
            onApplyClick = { selected ->
                // TODO: apply sort
            },
            onDismiss = { showSortOverlay.value = false }
        )

        FilterOverlay(
            visible = showFilterOverlay.value,
            onDismiss = { showFilterOverlay.value = false },
            onSubmitClick = {
                // apply filters
            }
        )

        ActionPopup(
            visible = showSavedPopup,
            text = "Saved",
            icon = Icons.Default.Bookmark,
            backgroundColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp),
            onClick = {
                onNavClick(AppScreen.SAVED_LISTINGS)
                // Navigate to saved items
            },
            onDismiss = {
                showSavedPopup = false
            }
        )


        NotificationsOverlay(
            visible = showNotifications,
            notifications = sampleNotifications,
            onClose = { showNotifications = false },
            onDismiss = { id ->
                // remove notification from list
            },
            onNotificationClick = { id ->
                // optional navigation
            }
        )

    }
}





