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
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.ui.components.OptionCard
import com.example.livingai_lg.ui.components.backgrounds.StoreBackground
import com.example.livingai_lg.R
data class ServiceType(
    val id: String,
    val title: String,
    val icon: Int,
    val backgroundColor: Color
)

@Composable
fun ChooseServiceScreen(
    profileId: String? = null,
    onServiceSelected: (serviceType: String) -> Unit = {}
) {
    val selectedService = remember { mutableStateOf<String?>(null) }

    val serviceTypes = listOf(
      ServiceType(
            id = "transport",
            title = "Transport",
            icon = R.drawable.ic_shop,
            backgroundColor = Color(0xFF9D4EDD)
        ),
        ServiceType(
            id = "vet",
            title = "Vet",
            icon = R.drawable.ic_bag,
            backgroundColor = Color(0xFF3A86FF)
        ),
        ServiceType(
            id = "feed_supplier",
            title = "Feed Supplier",
            icon = R.drawable.ic_spanner,
            backgroundColor = Color(0xFFFF5722)
        ),
        ServiceType(
            id = "medicine_supplier",
            title = "Medicine Supplier",
            icon = R.drawable.ic_shop2,
            backgroundColor = Color(0xFF4CAF50)
        ),
        ServiceType(
            id = "other",
            title = "Other",
            icon = R.drawable.ic_other,
            backgroundColor = Color(0xFFD4A942)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F1E8))
    ) {
        // Decorative background
        StoreBackground()

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Service",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose Service**",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A).copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Service selection cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                serviceTypes.forEach { service ->
                    OptionCard(
                        label = service.title,
                        icon = service.icon,
                        iconBackgroundColor = service.backgroundColor,
                        onClick = {
                            selectedService.value = service.id
                            onServiceSelected(service.id)
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun ServiceTypeCard(
    service: ServiceType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(service.backgroundColor, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (service.id) {
                    "transport" ->
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shop),
                            contentDescription = "Transport",
                        )

                    "vet" -> Icon(
                        painter = painterResource(id = R.drawable.ic_bag),
                        contentDescription = "Vet",
                    )
                    "feed_supplier" -> Icon(
                        painter = painterResource(id = R.drawable.ic_spanner),
                        contentDescription = "Feed Supplier",
                    )
                    "medicine_supplier" -> Icon(
                        painter = painterResource(id = R.drawable.ic_shop2),
                        contentDescription = "Medicine Supplier",
                    )
                    "other" -> Icon(
                        painter = painterResource(id = R.drawable.ic_other),
                        contentDescription = "Other",
                    )
                }
            }

            // Title
            Text(
                text = service.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF0A0A0A),
                modifier = Modifier.weight(1f)
            )

            // Radio button
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF6B7280),
                    unselectedColor = Color(0xFF6B7280)
                )
            )
        }
    }
}

