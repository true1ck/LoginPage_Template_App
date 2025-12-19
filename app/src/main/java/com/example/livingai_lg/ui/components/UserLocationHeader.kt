package com.example.livingai_lg.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.livingai_lg.ui.models.UserAddress
import com.example.livingai_lg.ui.models.UserProfile
import com.example.livingai_lg.R

@Composable
fun UserLocationHeader(
    user: UserProfile,
    selectedAddressId: String,
    modifier: Modifier = Modifier,
    onOpenAddressOverlay: () -> Unit,
    onProfileClick: () -> Unit = {}  // New callback for profile icon click
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val selectedAddress = user.addresses.find { it.id == selectedAddressId } ?: user.addresses.first()

        // Profile image - make it clickable
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onProfileClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (user.profileImageUrl != null) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Address + arrow (click opens overlay)
        Column(
            modifier = Modifier.clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onOpenAddressOverlay()
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedAddress.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = selectedAddress.address,
                fontSize = 13.sp,
                color = Color.Black.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

