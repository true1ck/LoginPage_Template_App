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
    modifier: Modifier = Modifier,
    onAddressSelected: (UserAddress) -> Unit = {},
    onAddNewClick: () -> Unit = {} // future navigation hook
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedAddress by remember {
        mutableStateOf(
            user.addresses.firstOrNull { it.isPrimary }
                ?: user.addresses.first()
        )
    }

    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Profile image
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black),
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

        // Anchor ONLY the text section
        Box {
            Column(
                modifier = Modifier
                    .clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { expanded = true }
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

            // Dropdown appears BELOW name/address
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                user.addresses.forEach { address ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = address.name,
                                fontWeight = if (address == selectedAddress)
                                    FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        },
                        onClick = {
                            selectedAddress = address
                            expanded = false
                            onAddressSelected(address)
                        }
                    )
                }

                Divider()

                // Add New option
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Add New +",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF007BFF)
                        )
                    },
                    onClick = {
                        expanded = false
                        onAddNewClick()
                    }
                )
            }
        }
    }
}
