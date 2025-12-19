package com.example.livingai_lg.ui.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.livingai_lg.R

data class ProfileType(
    val id: String,
    val title: String,
    val icon: Any,
    val iconTint: Color = Color.Unspecified,
    val backgroundColor: Color,
)

val profileTypes = listOf(
    ProfileType(
        id = "buyer_seller",
        title = "I'm a Buyer/Seller",
        icon = R.drawable.ic_shop,
        iconTint = Color.White,
        backgroundColor = Color(0xFF9D4EDD)
    ),
    ProfileType(
        id = "wholesale_trader",
        title = "I'm a Wholesale Trader",
        icon = R.drawable.ic_bag,
        iconTint = Color.White,
        backgroundColor = Color(0xFF3A86FF)
    ),
    ProfileType(
        id = "service_provider",
        title = "I'm a Service Provider",
        icon = R.drawable.ic_spanner,
        iconTint = Color.White,
        backgroundColor = Color(0xFFFF5722)
    ),
    ProfileType(
        id = "mandi_host",
        title = "I'm a Mandi Host",
        icon = R.drawable.ic_shop2,
        iconTint = Color.White,
        backgroundColor = Color(0xFF4CAF50)
    )
)
