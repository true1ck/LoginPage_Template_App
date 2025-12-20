package com.example.livingai_lg.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.livingai_lg.api.AuthApiClient
import com.example.livingai_lg.api.AuthManager
import com.example.livingai_lg.api.TokenManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    onApiTest: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthManager(context, AuthApiClient(context), TokenManager(context)) }
    val apiClient = remember { AuthApiClient(context) }
    
    // State for API test dialog
    var showApiResultDialog by remember { mutableStateOf(false) }
    var apiResultJson by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F4EE))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Accounts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF7F4EE)
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Logout option
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            authManager.logout()
                                .onSuccess { _: com.example.livingai_lg.api.LogoutResponse ->
                                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                                    onLogout()
                                }
                                .onFailure { error: Throwable ->
                                    Toast.makeText(context, "Logout failed: ${error.message}", Toast.LENGTH_SHORT).show()
                                    // Still call onLogout to navigate away even if API call fails
                                    onLogout()
                                }
                        }
                    },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Log Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 12.dp)
                    .clickable {
                        // Call API test directly instead of navigating
                        scope.launch {
                            isLoading = true
                            try {
                                // First get current user's ID
                                val userDetails = apiClient.getUserDetails().getOrNull()
                                if (userDetails != null) {
                                    // Call GET /users/:userId on port 3200
                                    val result = apiClient.getUserById(userDetails.id, "http://10.0.2.2:3200")
                                    result.onSuccess { jsonString ->
                                        apiResultJson = jsonString
                                        showApiResultDialog = true
                                        isLoading = false
                                    }.onFailure { error ->
                                        Toast.makeText(
                                            context,
                                            "API Test Failed: ${error.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        isLoading = false
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to get user details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isLoading = false
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                isLoading = false
                            }
                        }
                    },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Construction,
                                contentDescription = "Api test",
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = if (isLoading) "Testing API..." else "Test API",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
    
    // API Result Dialog
    if (showApiResultDialog) {
        Dialog(onDismissRequest = { showApiResultDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Dialog Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "API Test Result",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        IconButton(onClick = { showApiResultDialog = false }) {
                            Text(
                                text = "✕",
                                fontSize = 20.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    Divider()
                    
                    // JSON Content
                    apiResultJson?.let { json ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = json,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No result",
                                color = Color.Gray
                            )
                        }
                    }
                    
                    // Close Button
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { showApiResultDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

