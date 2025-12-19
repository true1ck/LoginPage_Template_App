package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.livingai_lg.ui.components.AdSpaceBanner
import com.example.livingai_lg.ui.layout.BottomNavScaffold
import com.example.livingai_lg.ui.models.mainBottomNavItems
import com.example.livingai_lg.ui.theme.AppTypography

data class SavedListing(
    val id: String,
    val name: String,
    val age: String,
    val breed: String,
    val price: String,
    val views: String,
    val distance: String,
    val imageUrl: String
)

enum class BottomNavTab {
    HOME,
    SELL,
    CHATS,
    SERVICES,
    MANDI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedListingsScreen(
    onListingClick: (String) -> Unit = {},
    onNavClick: (route: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val savedListings = listOf(
        SavedListing(
            id = "1",
            name = "Eauri",
            age = "Age: 3 yrs",
            breed = "Holstein",
            price = "₹45,000",
            views = "124 views",
            distance = "Distance: 12 km",
            imageUrl = "https://api.builder.io/api/v1/image/assets/TEMP/db216f496f6d5fd681db9afc8006eb8da3164c17?width=192"
        ),
        SavedListing(
            id = "2",
            name = "Fauri",
            age = "Age: 2 yrs",
            breed = "Jersey",
            price = "₹38,000",
            views = "89 views",
            distance = "Distance: 17 km",
            imageUrl = "https://api.builder.io/api/v1/image/assets/TEMP/a49aa5540b0cb6bf76ba3aa99cb8d4f94835e8ee?width=192"
        ),
        SavedListing(
            id = "3",
            name = "Gauri",
            age = "Age: 4 yrs",
            breed = "Gir",
            price = "₹52,000",
            views = "156 views",
            distance = "Distance: 20 km",
            imageUrl = "https://api.builder.io/api/v1/image/assets/TEMP/26fd38c965180ef066862fb1d0aa6df040a0d389?width=192"
        ),
        SavedListing(
            id = "4",
            name = "Hauri",
            age = "Age: 1.5 yrs",
            breed = "Sahiwal",
            price = "₹28,000",
            views = "67 views",
            distance = "Distance: 6 km",
            imageUrl = "https://api.builder.io/api/v1/image/assets/TEMP/b8edfd48d908a8e0eeeee55745ab409e454be25f?width=192"
        )
    )

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


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Saved Listings",
                            fontSize = AppTypography.Display,
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

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        AdSpaceBanner()
                    }

                    items(savedListings) { listing ->
                        ListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListingCard(
    listing: SavedListing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(138.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFFF3F4F6).copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color(0xFFFCFBF8), RoundedCornerShape(24.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 21.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = listing.imageUrl,
            contentDescription = listing.name,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = listing.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1E2939),
                lineHeight = 24.sp
            )

            Text(
                text = listing.age,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5565),
                lineHeight = 20.sp
            )

            Text(
                text = listing.breed,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5565),
                lineHeight = 20.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = listing.price,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1E2939),
                lineHeight = 24.sp,
                textAlign = TextAlign.End
            )

            Text(
                text = listing.views,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6A7282),
                lineHeight = 16.sp,
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = listing.distance,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A5565),
                lineHeight = 20.sp
            )
        }
    }
}