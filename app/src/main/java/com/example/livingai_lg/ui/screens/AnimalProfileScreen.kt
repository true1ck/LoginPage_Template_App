package com.example.livingai_lg.ui.screens

import com.example.livingai_lg.ui.components.ImageCarousel
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.example.livingai_lg.ui.models.Animal
import com.example.livingai_lg.ui.utils.formatPrice
import com.example.livingai_lg.ui.utils.formatViews
import com.example.livingai_lg.ui.components.AdSpaceBanner
import com.example.livingai_lg.ui.components.FloatingActionBar
import com.example.livingai_lg.ui.models.sampleAnimals
import com.example.livingai_lg.ui.utils.formatAge
import com.example.livingai_lg.R


@Composable
fun AnimalProfileScreen(
    animalId: String,
    onBackClick: () -> Unit = {},
    onSellerClick: (sellerId: String) -> Unit = {},
) {
    val animal = sampleAnimals.find { animal -> animal.id == animalId } ?: Animal(id = "null")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F4EE))
                .verticalScroll(rememberScrollState())
        ) {
            // Photo section with overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .shadow(4.dp)
            ) {
                // Main image
                val product = null
                ImageCarousel(
                    imageUrls = animal.imageUrl ?: emptyList(),
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                // Views indicator (top left)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_view),
                        contentDescription = "Views",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = formatViews(animal.views),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }

                // Animal info (centered bottom)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 68.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${animal.name}, ${formatAge(animal.age)}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                    Text(
                        text = "${animal.breed} • ${animal.location}, ${animal.distance}".uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.2.sp
                    )
                }

                // AI Score badge (bottom left)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 0.dp, bottom = 5.dp)
                        .height(48.dp)
                        .border(2.dp, Color(0xFF717182), CircleShape)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AIScoreCircle(score = animal.aiScore ?: 0f)
                    Text(
                        text = "AI Score",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }

                // Display location (bottom right)
                Text(
                    text = buildString {
                        append("At Display: ")
                        append(animal.displayLocation)
                    },
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    lineHeight = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 8.dp)
                        .width(98.dp)
                )
            }

            // Info card section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Price and seller info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatPrice(animal.price),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0A0A0A)
                        )

                        if (animal.isFairPrice ?: false) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 0.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_thumbs_up),
                                    contentDescription = "Fair Price",
                                    tint = Color(0xFF0A0A0A),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Fair Price",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF00C950)
                                )
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable(
                                indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onSellerClick(animal.sellerId?:"")
                    })
                     {
                        Text(
                            text = "Sold By: ${animal.sellerName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF0A0A0A)
                        )

                        // Star rating
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    painter = painterResource(R.drawable.ic_star),
                                    contentDescription = null,
                                    tint = if (index < (animal.rating ?: 0).toInt()) Color(
                                        0xFFDE9A07
                                    ) else Color(0xFFDE9A07),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Text(
                                text = "${animal.rating} (${animal.ratingCount} Ratings)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF0A0A0A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // About section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "About",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF09090B).copy(alpha = 0.5f)
                    )
                    Text(
                        text = animal.description ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF09090B),
                        lineHeight = 24.sp
                    )
                }

//                Spacer(modifier = Modifier.height(24.dp))
//
//
//                FloatingActionBar(
//                    modifier = Modifier,
//                    // .offset(y = (-40).dp), // 👈 hover effect
//                    onChatClick = { /* TODO */ },
//                    onCallClick = { /* TODO */ },
//                    onLocationClick = { /* TODO */ },
//                    onBookmarkClick = { /* TODO */ }
//                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ad space banner
                AdSpaceBanner()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        FloatingActionBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .offset(y = (-20).dp)
                .zIndex(10f), // 👈 ensure it floats above everything
            onChatClick = { /* TODO */ },
            onCallClick = { /* TODO */ },
            onLocationClick = { /* TODO */ },
            onBookmarkClick = { /* TODO */ }
        )

    }
}

@Composable
fun AIScoreCircle(score: Float) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .drawBehind {
                // Background circle
                drawCircle(
                    color = Color(0xFFDD88CF),
                    radius = size.minDimension / 2,
                    alpha = 0.3f,
                    style = Stroke(width = 4.dp.toPx())
                )
                
                // Progress arc
                val sweepAngle = 360f * score
                drawArc(
                    color = Color(0xFF9AFF9A),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height),
                    topLeft = Offset.Zero
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${(score * 100).toInt()}%",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}

@Composable
fun ActionButtonLarge(icon: Int, label: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(8.dp, CircleShape)
            .background(Color.White, CircleShape),
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
