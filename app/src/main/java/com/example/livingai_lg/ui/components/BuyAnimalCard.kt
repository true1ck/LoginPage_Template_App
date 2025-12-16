package com.example.livingai_lg.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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

@Composable
fun BuyAnimalCard(
    product: Animal,
    isSaved: Boolean,
    onSavedChange: (Boolean) -> Unit,
    onProductClick: () -> Unit,
    onSellerClick:(sellerId: String)-> Unit,
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
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onProductClick
            )
    ) {
        Column {
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
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                       painter = painterResource(R.drawable.ic_view),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(formatViews(product.views), fontSize = 10.sp, color = Color.White)
                }

                // Distance
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(formatDistance( product.distance), fontSize = 16.sp, color = Color.White)
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
                        Text(
                            product.name?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            formatPrice(product.price),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(horizontalAlignment = Alignment.End,modifier = Modifier.clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onSellerClick(product.sellerId?:"")
                    }) {
                        Text("Sold By: ${product.sellerName}", fontSize = 13.sp)
                        Text(product.sellerType?: "???", fontSize = 13.sp)
                    }
                }

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = Color(0xFFDE9A07),
                        modifier = Modifier.size(8.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${product.rating} (${product.ratingCount} Ratings)",
                        fontSize = 8.sp
                    )
                }

                // Badges
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            val scoreString = "AI Score: ${product.aiScore?: 0}"
                            Text(scoreString, fontSize = 12.sp)
                        }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("placeholder", fontSize = 12.sp)
                    }
                    if(product.milkCapacity != null) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text("Milk Capacity: ${product.milkCapacity}L", fontSize = 12.sp)
                        }
                    }
                }

                // Description
                Text(
                    product.description?: "",
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
                FloatingActionBar(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .zIndex(10f), // 👈 ensure it floats above everything
                    onChatClick = { /* TODO */ },
                    onCallClick = { /* TODO */ },
                    onLocationClick = { /* TODO */ },
                    onBookmarkClick = { /* TODO */ }
                )


            }
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
