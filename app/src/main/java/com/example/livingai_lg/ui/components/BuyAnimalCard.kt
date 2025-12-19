package com.example.livingai_lg.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.livingai_lg.ui.screens.*
import com.example.livingai_lg.ui.models.Animal
import com.example.livingai_lg.ui.utils.formatDistance
import com.example.livingai_lg.ui.utils.formatPrice
import com.example.livingai_lg.ui.utils.formatViews
import com.example.livingai_lg.R
import com.example.livingai_lg.ui.theme.AppTypography

@Composable
fun BuyAnimalCard(
    product: Animal,
    isSaved: Boolean,
    onSavedChange: (Boolean) -> Unit,
    onProductClick: () -> Unit,
    onSellerClick:(sellerId: String)-> Unit,
    onBookmarkClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(1.078.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(
                1.078.dp,
                Color(0xFF000000).copy(alpha = 0.1f),
                RoundedCornerShape(14.dp)
            )

    ) {
        Column {
            Column(
                modifier = Modifier.clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onProductClick
                )
            ) {

                // Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(257.dp)
                ) {
                    ImageCarousel(
                        imageUrls = product.imageUrl ?: emptyList(),
                        modifier = Modifier.fillMaxSize()
                    )

                    // Views
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(50),
                                ambientColor = Color.Black.copy(alpha = 0.4f),
                                spotColor = Color.Black.copy(alpha = 0.4f)
                            )
                            .background(
                                color = Color.Black.copy(alpha = 0.35f), // 👈 light but effective
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp).shadow(
                                elevation = 6.dp,
                                shape = CircleShape,
                                clip = false
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(formatViews(product.views), fontSize = AppTypography.Caption, color = Color.White,style = LocalTextStyle.current.copy(
                        ))
                    }

                    // Distance
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(50),
                                ambientColor = Color.Black.copy(alpha = 0.4f),
                                spotColor = Color.Black.copy(alpha = 0.4f)
                            )
                            .background(
                                color = Color.Black.copy(alpha = 0.35f), // 👈 light but effective
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            formatDistance(product.distance),
                            fontSize = AppTypography.Body,
                            color = Color.White,
                        )
                    }
                }

                // Content
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row {
                                Text(
                                    product.breed ?: "",
                                    fontSize = AppTypography.Body,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    Modifier.padding(horizontal = 4.dp).size(16.dp).align(Alignment.CenterVertically),

                                )

                                //InfoIconWithOverlay(infoText = product.breedInfo ?: "")
                            }

                                Text(
                                formatPrice(product.price),
                                fontSize = AppTypography.Body,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End, modifier = Modifier.clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onSellerClick(product.sellerId ?: "")
                        }) {
                            Text("Sold By: ${product.sellerName}", fontSize = AppTypography.BodySmall)
                            Text(product.sellerType ?: "???", fontSize = AppTypography.BodySmall)
                        }
                    }

                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingStars(product.rating?:0f, starSize = 12.dp)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${product.rating} (${product.ratingCount} Ratings)",
                            fontSize = AppTypography.Caption
                        )
                    }

                    // Badges
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row {
                                Text("AI Score: ", fontSize = AppTypography.Caption)
                                val scoreString = "${product.aiScore ?: 0}"
                                Text(scoreString, fontSize = AppTypography.Caption, fontWeight = FontWeight.Bold)

                            }
                            }

                        if (product.milkCapacity != null) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Row {
                                    Text(
                                        "Milk Capacity:",
                                        fontSize = AppTypography.Caption
                                    )
                                    Text(
                                        "${product.milkCapacity}L",
                                        fontSize = AppTypography.Caption,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    Text(
                        product.description ?: "",
                        fontSize = 14.sp,
                        color = Color(0xFF717182),
                        lineHeight = 20.sp
                    )

                    // Actions
//                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                    ActionButton(R.drawable.ic_chat, "Chat")
//                    ActionButton(R.drawable.ic_phone, "Call")
//                    ActionButton(R.drawable.ic_location, "Location")
//                    ActionButton(R.drawable.ic_bookmark_plus, "Bookmark")
//                }


                }
            }
            FloatingActionBar(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .zIndex(10f), // 👈 ensure it floats above everything
                showContainer = false,
                onChatClick = { /* TODO */ },
                onCallClick = { /* TODO */ },
                onLocationClick = { /* TODO */ },
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}

@Composable
fun ActionButton(icon: Int, label: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = Color(0xFF0A0A0A),
            modifier = Modifier.size(20.dp)
        )
    }
}
