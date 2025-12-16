package com.example.livingai_lg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SellerProfile(
    val name: String,
    val initials: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val trustScore: Int,
    val totalSales: Int,
    val yearsActive: String,
    val responseRate: String,
    val about: String
)

data class PastSale(
    val id: String,
    val name: String,
    val emoji: String,
    val age: String,
    val breed: String,
    val price: String,
    val rating: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProfileScreen(
    sellerId: String = "1",
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onViewAllSalesClick: () -> Unit = {}
) {
    // Sample data - in real app, fetch based on sellerId
    val seller = SellerProfile(
        name = "Ramesh Singh",
        initials = "RS",
        location = "Nagpur, Maharashtra",
        rating = 4.8,
        reviewCount = 156,
        trustScore = 95,
        totalSales = 47,
        yearsActive = "5+",
        responseRate = "98%",
        about = "Experienced cattle farmer and trader with over 5 years in the business. Specializing in dairy cattle breeds including Gir, Holstein, and Jersey. All animals are well-maintained with complete vaccination records and health certificates."
    )

    val pastSales = listOf(
        PastSale("1", "Gir Cow", "🐄", "Age: 3 yrs", "Holstein", "₹45,000", 5.0),
        PastSale("2", "Buffalo", "🐃", "Age: 4 yrs", "Murrah", "₹55,000", 4.9),
        PastSale("3", "Goat", "🐐", "Age: 2 yrs", "Sirohi", "₹12,000", 4.7)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Seller Profile",
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
            ),
            modifier = Modifier.border(
                width = 1.dp,
                color = Color(0x1A000000),
                shape = RectangleShape
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0x1A000000))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color(0xFFE5E7EB), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = seller.initials,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF0A0A0A)
                            )
                        }

                        // Info
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = seller.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF0A0A0A)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = Color(0xFF717182),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = seller.location,
                                    fontSize = 14.sp,
                                    color = Color(0xFF717182)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFDC700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = seller.rating.toString(),
                                        fontSize = 14.sp,
                                        color = Color(0xFF0A0A0A)
                                    )
                                }

                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = Color(0xFF717182)
                                )

                                Text(
                                    text = "${seller.reviewCount} reviews",
                                    fontSize = 14.sp,
                                    color = Color(0xFF717182)
                                )
                            }
                        }
                    }

                    // AI Trust Score
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0x4D60A5FA),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                Color(0x4D60A5FA),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Trust",
                                        tint = Color(0xFF155DFC),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "AI Trust Score",
                                        fontSize = 14.sp,
                                        color = Color(0xFF0A0A0A)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Score",
                                        tint = Color(0xFF155DFC),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${seller.trustScore}/100",
                                        fontSize = 16.sp,
                                        color = Color(0xFF155DFC)
                                    )
                                }
                            }

                            Text(
                                text = "Verified seller with excellent transaction history",
                                fontSize = 12.sp,
                                color = Color(0xFF717182)
                            )
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onCallClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Call",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        OutlinedButton(
                            onClick = onMessageClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(23.dp),
                            border = BorderStroke(
                                1.dp,
                                Color(0x1A000000)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFF0A0A0A)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Message,
                                contentDescription = "Message",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Message",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = seller.totalSales.toString(),
                    label = "Total Sales",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = seller.yearsActive,
                    label = "Years Active",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = seller.responseRate,
                    label = "Response\nRate",
                    modifier = Modifier.weight(1f)
                )
            }

            // Past Sales Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Sales",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF0A0A0A)
                    )

                    TextButton(onClick = onViewAllSalesClick) {
                        Text(
                            text = "View All",
                            fontSize = 14.sp,
                            color = Color(0xFF717182),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                pastSales.forEach { sale ->
                    PastSaleCard(sale)
                }
            }

            // About Seller Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "About Seller",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF0A0A0A)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0x1A000000))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = seller.about,
                            fontSize = 14.sp,
                            lineHeight = 22.75.sp,
                            color = Color(0xFF717182)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(94.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0x1A000000))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF0A0A0A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF717182),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PastSaleCard(sale: PastSale) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0x1A000000))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animal Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF7F4EE), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sale.emoji,
                    fontSize = 24.sp
                )
            }

            // Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sale.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF0A0A0A)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = sale.age,
                        fontSize = 12.sp,
                        color = Color(0xFF717182)
                    )
                    Text(
                        text = sale.breed,
                        fontSize = 12.sp,
                        color = Color(0xFF717182)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sale.price,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF0A0A0A)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFDC700),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = sale.rating.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF717182)
                        )
                    }
                }
            }
        }
    }
}
