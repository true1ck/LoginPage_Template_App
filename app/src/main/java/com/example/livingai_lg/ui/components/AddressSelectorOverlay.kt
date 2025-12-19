package com.example.livingai_lg.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.models.UserAddress

@Composable
fun AddressSelectorOverlay(
    visible: Boolean,
    addresses: List<UserAddress>,
    selectedAddressId: String?,
    onSelect: (String) -> Unit,
    onClose: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
        ) {

            Column {
                AddressSelectorHeader(onClose)
                AddressSearchBar()
                AddressActionsRow()
                SavedAddressesList(
                    addresses = addresses,
                    selectedAddressId = selectedAddressId,
                    onSelect = onSelect
                )
            }
        }
    }
}


@Composable
fun AddressSelectorHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Select Your Location",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AddressSearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search an area or address") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        enabled = false // 👈 explicitly disabled for now
    )
}

@Composable
fun AddressActionsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AddressActionCard(Icons.Default.MyLocation,"Use Current Location")
        AddressActionCard(Icons.Default.LibraryAdd,"Add New Address")
    }
}

@Composable
fun SavedAddressesList(
    addresses: List<UserAddress>,
    selectedAddressId: String?,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "SAVED ADDRESSES",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(8.dp))

        addresses.forEach { address ->
            AddressItem(
                address = address,
                selected = address.id == selectedAddressId,
                onClick = { onSelect(address.id) }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun AddressItem(
    address: UserAddress,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Home, contentDescription = null)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(address.name, fontWeight = FontWeight.Medium)
                if (selected) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "SELECTED",
                        fontSize = 10.sp,
                        color = Color.Green
                    )
                }
            }
            Text(address.address, fontSize = 12.sp, color = Color.Gray)
        }

        Icon(Icons.Default.MoreVert, contentDescription = null)
    }
}

@Composable
fun AddressActionCard(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .size(110.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

