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
import com.example.livingai_lg.ui.theme.AppTypography

data class ServiceType(
    val id: String,
    val title: String,
    val icon: Any,
    val iconTint: Color,
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
            iconTint = Color.White,
            backgroundColor = Color(0xFF9D4EDD)
        ),
        ServiceType(
            id = "vet",
            title = "Vet",
            icon = R.drawable.ic_bag,
            iconTint = Color.White,
            backgroundColor = Color(0xFF3A86FF)
        ),
        ServiceType(
            id = "feed_supplier",
            title = "Feed Supplier",
            icon = R.drawable.ic_spanner,
            iconTint = Color.White,
            backgroundColor = Color(0xFFFF5722)
        ),
        ServiceType(
            id = "medicine_supplier",
            title = "Medicine Supplier",
            icon = R.drawable.ic_shop2,
            iconTint = Color.White,
            backgroundColor = Color(0xFF4CAF50)
        ),
        ServiceType(
            id = "other",
            title = "Other",
            icon = R.drawable.ic_other,
            iconTint = Color.White,
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
                    fontSize = AppTypography.Display,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose a Service",
                    fontSize = AppTypography.Body,
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
                        iconTint = service.iconTint,
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

