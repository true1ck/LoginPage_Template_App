package com.example.livingai_lg.ui.screens

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class SaleArchive(
    val id: String,
    val name: String,
    val breed: String,
    val soldTo: String,
    val saleDate: String,
    val salePrice: String,
    val location: String,
    val notes: String,
    val imageUrl: String,
    val attachments: List<Attachment>
)

data class Attachment(
    val id: String,
    val name: String,
    val type: AttachmentType
)

enum class AttachmentType {
    SALE_PAPERS,
    PHOTO
}

@Composable
fun SaleArchiveScreen(
    saleId: String,
    onBackClick: () -> Unit = {},
    onSaleSurveyClick:(saleId: String) -> Unit = {},
) {
    val sale = SaleArchive(
        id = "1",
        name = "Gauri",
        breed = "Gir",
        soldTo = "Buyer 1",
        saleDate = "07.12.2025",
        salePrice = "₹30,000",
        location = "Ravi Nagar, Nagpur",
        notes = "Buyer requested a check up after 15 days of sale. Additional support for 20 days.",
        imageUrl = "https://api.builder.io/api/v1/image/assets/TEMP/9389ccf57f6d262b500be45f6d9cbfe929302413?width=784",
        attachments = listOf(
            Attachment("1", "Sale Papers 1", AttachmentType.SALE_PAPERS),
            Attachment("2", "Photo 1", AttachmentType.PHOTO),
            Attachment("3", "Sale Papers 1", AttachmentType.SALE_PAPERS),
            Attachment("4", "Photo 2", AttachmentType.PHOTO)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 11.dp, vertical = 15.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0A0A0A),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onBackClick
                        )
                )
            }

            AsyncImage(
                model = sale.imageUrl,
                contentDescription = "${sale.name} image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(273.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 25.dp,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        spotColor = Color.Black.copy(alpha = 0.25f)
                    )
                    .border(
                        width = 1.078.dp,
                        color = Color(0xFFE5E7EB).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .background(
                        color = Color(0xFFFCFBF8),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(horizontal = 21.dp, vertical = 17.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(1.987.dp)
                ) {
                    Text(
                        text = "Name, Breed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6A7282),
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "${sale.name}, (${sale.breed})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF101828),
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(11.99.dp)
                ) {
                    InfoField(
                        label = "Sold To",
                        value = sale.soldTo,
                        modifier = Modifier.weight(1f)
                    )
                    InfoField(
                        label = "Sale Date",
                        value = sale.saleDate,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 17.dp),
                    horizontalArrangement = Arrangement.spacedBy(11.99.dp)
                ) {
                    InfoField(
                        label = "Sale Price",
                        value = sale.salePrice,
                        modifier = Modifier.weight(1f)
                    )
                    InfoField(
                        label = "Location",
                        value = sale.location,
                        modifier = Modifier.weight(1f)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.078.dp)
                        .background(Color(0xFFD1D5DC))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.995.dp)
                ) {
                    Text(
                        text = "Notes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6A7282),
                        lineHeight = 16.sp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = sale.notes,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF364153),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Attachments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF000000),
                        lineHeight = 24.sp
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(11.dp)
                        ) {
                            AttachmentButton(
                                name = sale.attachments[0].name,
                                modifier = Modifier.weight(1f)
                            )
                            AttachmentButton(
                                name = sale.attachments[1].name,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(11.dp)
                        ) {
                            AttachmentButton(
                                name = sale.attachments[2].name,
                                modifier = Modifier.weight(1f)
                            )
                            AttachmentButton(
                                name = sale.attachments[3].name,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InfoField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(1.987.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6A7282),
            lineHeight = 16.sp
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF101828),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun AttachmentButton(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(38.dp)
            .border(
                width = 1.078.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF4A5565),
            lineHeight = 20.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.width(5.995.dp))
        Icon(
            imageVector = Icons.Default.AttachFile,
            contentDescription = "Attachment",
            tint = Color(0xFFADADAD),
            modifier = Modifier.size(19.99.dp)
        )
    }
}