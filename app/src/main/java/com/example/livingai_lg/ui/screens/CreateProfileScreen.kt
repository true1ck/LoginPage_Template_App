package com.example.livingai_lg.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.TokenManager
import com.example.livingai_lg.ui.components.OptionCard

import com.example.livingai_lg.ui.components.backgrounds.StoreBackground
import com.example.livingai_lg.ui.models.profileTypes
import kotlinx.coroutines.launch


@Composable
fun CreateProfileScreen(
    name: String,
    onProfileSelected: (profileId: String) -> Unit = {}
) {
    val selectedProfile = remember { mutableStateOf<String>(profileTypes[0].id) }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }

    fun updateProfile(profileId: String) {
        scope.launch {
            authManager.updateProfile(name, profileId)
                .onSuccess {
                    onProfileSelected(profileId)
                }
                .onFailure {
                    Toast.makeText(context, "Failed to update profile: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
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
                    text = "Create Profile",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose Profile Type",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A).copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Profile selection cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                profileTypes.forEach { profile ->
                    OptionCard(
                        label = profile.title,
                        icon = profile.icon,
                        iconBackgroundColor = profile.backgroundColor,
                        onClick = {
                            updateProfile(profile.id)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
