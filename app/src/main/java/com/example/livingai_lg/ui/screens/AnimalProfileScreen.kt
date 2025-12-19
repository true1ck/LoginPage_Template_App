package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.Image
import com.example.livingai_lg.ui.components.ImageCarousel
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.example.livingai_lg.ui.models.Animal
import com.example.livingai_lg.ui.utils.formatPrice
import com.example.livingai_lg.ui.utils.formatViews
import com.example.livingai_lg.ui.components.AdSpaceBanner
import com.example.livingai_lg.ui.components.FloatingActionBar
import com.example.livingai_lg.ui.models.sampleAnimals
import com.example.livingai_lg.ui.utils.formatAge
import com.example.livingai_lg.R
import com.example.livingai_lg.ui.components.ActionPopup
import com.example.livingai_lg.ui.components.RatingStars
import com.example.livingai_lg.ui.navigation.AppScreen
import com.example.livingai_lg.ui.theme.AppTypography
import com.example.livingai_lg.ui.utils.formatDistance


@Composable
fun AnimalProfileScreen(
    animalId: String,
    onBackClick: () -> Unit = {},
    onSellerClick: (sellerId: String) -> Unit = {},
    onNavClick: (route: String) -> Unit = {}
) {
    var showSavedPopup by remember { mutableStateOf(false) }
    val animal = sampleAnimals.find { animal -> animal.id == animalId } ?: Animal(id = "null")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
            .padding(12.dp)
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
                    .height(350.dp)
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
                        .padding(start = 5.dp, top = 5.dp)
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
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_view),
                        contentDescription = "Views",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = formatViews(animal.views),
                        fontSize = AppTypography.BodySmall,
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
                        fontSize = AppTypography.Title,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        style = LocalTextStyle.current.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.75f),
                                offset = Offset(0f, 2f),
                                blurRadius = 6f
                            )
                        )
                    )
                    Text(
                        text = "${animal.breed} • ${animal.location}, ${formatDistance(animal.distance)}".uppercase(),
                        fontSize = AppTypography.Body,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.2.sp,
                        style = LocalTextStyle.current.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.6f),
                                offset = Offset(0f, 1.5f),
                                blurRadius = 4f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                }

                // AI Score badge (bottom left)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 5.dp, bottom = 5.dp)
                        .height(60.dp)
                        .border(2.dp, Color(0xFF717182), CircleShape)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AIScoreCircle(score = animal.aiScore ?: 0f)
                    Text(
                        text = "AI Score",
                        fontSize = AppTypography.Body,
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
                    fontSize = AppTypography.Caption,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    lineHeight = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Right,
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
                            fontSize = AppTypography.Title,
                            fontWeight = FontWeight.Bold,
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
                                    tint = Color(0xFF00C950),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Fair Price",
                                    fontSize = AppTypography.Caption,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF00C950)
                                )
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(
                                indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onSellerClick(animal.sellerId?:"")
                    })
                     {
                        Text(
                            text = "Sold By: ${animal.sellerName}",
                            fontSize = AppTypography.Body,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF0A0A0A)
                        )

                        // Star rating
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingStars(
                                rating = animal.rating ?: 0f,
                                starSize = 12.dp
                            )
                            Text(
                                text = "${animal.rating} (${animal.ratingCount} Ratings)",
                                fontSize = AppTypography.Caption,
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
                        fontSize = AppTypography.Body,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF09090B).copy(alpha = 0.5f)
                    )
                    Text(
                        text = animal.description ?: "",
                        fontSize = AppTypography.BodySmall,
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

                Spacer(modifier = Modifier.height(64.dp))
            }
        }
        FloatingActionBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .offset(y = (-10).dp)
                .zIndex(10f), // 👈 ensure it floats above everything
            onChatClick = { /* TODO */ },
            onCallClick = { /* TODO */ },
            onLocationClick = { /* TODO */ },
            onBookmarkClick = { showSavedPopup = true }
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
    }

}

fun Modifier.Companion.align(bottomEnd: Alignment) {}

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
